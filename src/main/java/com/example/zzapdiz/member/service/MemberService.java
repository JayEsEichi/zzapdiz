package com.example.zzapdiz.member.service;

import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.jwt.domain.Token;
import com.example.zzapdiz.jwt.dto.TokenDto;
import com.example.zzapdiz.jwt.repository.TokenRepository;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.repository.MemberRepository;
import com.example.zzapdiz.member.request.MemberLoginRequestDto;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.share.DynamicQueryDsl;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberExceptionInterface memberExceptionInterface;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final DynamicQueryDsl dynamicQueryDsl;
    private final TokenRepository tokenRepository;

    // 회원가입
    public ResponseEntity<ResponseBody> memberSignUp(MemberSignupRequestDto memberSignupRequestDto) {

        // 회원가입 시 발생할 에러 처리
        memberExceptionInterface.duplicatedEmailCheck(memberSignupRequestDto.getEmail());
        memberExceptionInterface.matchPassword(memberSignupRequestDto.getPassword(), memberSignupRequestDto.getPasswordRecheck());
        memberExceptionInterface.alreadyExistMember(memberSignupRequestDto.getEmail());
        memberExceptionInterface.allRequestDataCheck(memberSignupRequestDto);

        // 회원가입 정보 입력
        Member member = Member.builder()
                .memberName(memberSignupRequestDto.getMemberName())
                .email(memberSignupRequestDto.getEmail())
                .password(passwordEncoder.encode(memberSignupRequestDto.getPassword()))
                .point(0)
                .build();

        // 회원가입
        memberRepository.save(member);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "회원 가입이 완료되셨습니다. 어서오세요! ^^"), HttpStatus.OK);
    }


    // 로그인
    public ResponseEntity<ResponseBody> memberLogin(HttpServletResponse response, MemberLoginRequestDto memberLoginRequestDto){

        // 로그인 시 발생할 에러 처리
        memberExceptionInterface.checkEmail(memberLoginRequestDto.getEmail());
        memberExceptionInterface.checkPassword(memberLoginRequestDto.getPassword(), dynamicQueryDsl.findMemberByEmail(memberLoginRequestDto.getEmail()).getPassword());

        // re 로그인 시 기존에 남아있던 토큰 삭제
        dynamicQueryDsl.reLoginDeleteToken(dynamicQueryDsl.findMemberByEmail(memberLoginRequestDto.getEmail()).getMemberId());

        // 토큰을 발급하고 Dto 개체에 저장하는 과정
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // Response Header에 액세스 토큰 리프레시 토큰, 토큰 만료일 input
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        // 발급된 토큰 정보를 토대로 Token 엔티티에 input
        Token token = Token.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .memberId(dynamicQueryDsl.findMemberByEmail(memberLoginRequestDto.getEmail()).getMemberId())
                .build();

        // 토큰 저장
        tokenRepository.save(token);

        // postman으로 확인하기 위한 결과 세트
        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("loginCompleteMessage", "로그인 되셨습니다!");
        resultSet.put("accessToken", response.getHeader("Authorization"));
        resultSet.put("refreshToken", response.getHeader("Refresh-Token"));

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }
}
