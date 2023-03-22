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
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.example.zzapdiz.share.mail.MailDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Properties;

@Slf4j
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
        if(memberExceptionInterface.duplicatedEmailCheck(memberSignupRequestDto.getEmail())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.DUPLICATED_ACCOUNT, null), HttpStatus.BAD_REQUEST);
        }
        if(memberExceptionInterface.matchPassword(memberSignupRequestDto.getPassword(), memberSignupRequestDto.getPasswordRecheck())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.NOT_MATCH_PASSWORD, null), HttpStatus.BAD_REQUEST);
        }
        if(memberExceptionInterface.alreadyExistMember(memberSignupRequestDto.getEmail())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.ALREADY_EXIST_MEMBER, null), HttpStatus.BAD_REQUEST);
        }
        if(memberExceptionInterface.allRequestDataCheck(memberSignupRequestDto)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_DATA, null), HttpStatus.BAD_REQUEST);
        }

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
        if(memberExceptionInterface.checkEmail(memberLoginRequestDto.getEmail())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.NOT_FOUND_MATCHING_EMAIL, null), HttpStatus.BAD_REQUEST);
        }
        if(memberExceptionInterface.checkPassword(memberLoginRequestDto.getPassword(), dynamicQueryDsl.findMemberByEmail(memberLoginRequestDto.getEmail()).getPassword())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.NOT_FOUND_MATCHING_PASSWORD, null), HttpStatus.BAD_REQUEST);
        }

        // re 로그인 시 기존에 남아있던 토큰 삭제
        dynamicQueryDsl.deleteToken(dynamicQueryDsl.findMemberByEmail(memberLoginRequestDto.getEmail()).getMemberId());

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


    // 로그아웃
    public ResponseEntity<ResponseBody> memberLogout(HttpServletRequest request) throws ServletException {

        // 로그아웃 시 토큰 확인
        if(memberExceptionInterface.checkHeaderToken(request)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 인증받은 회원의 객체 조회
        // SecurityContextHolder에 저장된 인증된 회원 정보에서 이메일을 추출하여 객체 반환
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();

        // 로그아웃 시 Token 엔티티에 저장된 토큰 정보 삭제
        dynamicQueryDsl.deleteToken(authMember.getMemberId());

        // HTTPServletRequest 에서 제공하는 logout 함수를 사용하여 로그아웃한 회원 계정의 인증 삭제
        request.logout();

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "정상적으로 로그아웃 되셨습니다. 이용해주셔서 감사합니다 ^^"), HttpStatus.OK);
    }


    // 회원탈퇴
    public ResponseEntity<ResponseBody> memberSignOut(HttpServletRequest request) {

        // 로그아웃 시 토큰 확인
        if(memberExceptionInterface.checkHeaderToken(request)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 인증받은 회원의 객체 조회
        // SecurityContextHolder에 저장된 인증된 회원 정보에서 이메일을 추출하여 객체 반환
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();

        // 회원탈퇴 시 Token 엔티티와 해당 Member 엔티티에 저장된 데이터 삭제
        dynamicQueryDsl.deleteToken(authMember.getMemberId());
        dynamicQueryDsl.deleteMember(authMember.getMemberId());

        // HttpRequest에 남아있는 토큰 정보들 삭제
        request.removeAttribute("Authorization");
        request.removeAttribute("Refresh-Token");
        request.removeAttribute("Access-Token-Expire-Time");

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "정상적으로 회원탈퇴되었습니다. 다음에 다시 만나뵙기를 기대하고 있겠습니다."), HttpStatus.OK);
    }


    // 아이디 찾기 (아이디 확인) 메일 발신
    public ResponseEntity<ResponseBody> findMemberEmail(MailDto mailDto){

        // 아이디를 찾고자 입력한 이메일이 존재하지 않을 경우
        memberExceptionInterface.checkEmail(mailDto.getMailAddress());

        JavaMailSender javaMailSender = MailService(mailDto.getMailAddress());
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(mailDto.getMailAddress());
        mailMessage.setSubject("[ZZAPDIZ] 아이디 확인 메일입니다.");
        mailMessage.setText("아이디 : " + dynamicQueryDsl.findMemberByEmail(mailDto.getMailAddress()).getEmail());
        javaMailSender.send(mailMessage);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "아이디 확인 메일 발신 완료"), HttpStatus.OK);
    }


    private JavaMailSender MailService(String domain) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties properties = new Properties();

        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.ssl.enable","true");
        properties.setProperty("mail.smtp.starttls.enable","true");

        // 가입한 계정이 네이버의 경우
        if(domain.contains("naver")){
            properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");

            javaMailSender.setHost("smtp.naver.com");
            javaMailSender.setUsername(domain);
            javaMailSender.setPassword("wls124578!");
            javaMailSender.setPort(465);
            javaMailSender.setJavaMailProperties(properties);
        }else if(domain.contains("google")){
            properties.setProperty("mail.smtp.ssl.trust","smtp.google.com");

            javaMailSender.setHost("smtp.google.com");
            javaMailSender.setUsername(domain);
            javaMailSender.setPassword("wls12457!!");
            javaMailSender.setPort(587);
            javaMailSender.setJavaMailProperties(properties);
        }

        return javaMailSender;
    }

}
