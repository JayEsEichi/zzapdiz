package com.example.zzapdiz.member.service;

import com.example.zzapdiz.exception.MemberExceptionInterface;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.repository.MemberRepository;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberExceptionInterface memberExceptionInterface;

    // 회원가입
    public ResponseEntity<ResponseBody> memberSignUp(MemberSignupRequestDto memberSignupRequestDto) {

        // 회원가입 시 발생할 에러 처리
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
}
