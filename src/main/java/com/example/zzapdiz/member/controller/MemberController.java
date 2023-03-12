package com.example.zzapdiz.member.controller;

import com.example.zzapdiz.member.request.MemberLoginRequestDto;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.member.service.MemberService;
import com.example.zzapdiz.share.MailDto;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/zzapdiz")
@RestController
public class MemberController {

    private final MemberService memberService;

    /** 회원가입 **/
    @PostMapping("/member/signup")
    public ResponseEntity<ResponseBody> memberSignUp(@RequestBody MemberSignupRequestDto memberSignupRequestDto){
        log.info("회원가입 api : 이메일 - {}, 이름 - {}", memberSignupRequestDto.getEmail(), memberSignupRequestDto.getMemberName());

        return memberService.memberSignUp(memberSignupRequestDto);
    }


    /** 로그인 **/
    @GetMapping("/member/login")
    public ResponseEntity<ResponseBody> memberLogin(HttpServletResponse response, @RequestBody MemberLoginRequestDto memberLoginRequestDto){
        log.info("로그인 api : 이메일 - {}, 비밀번호 - {}", memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword());

        return memberService.memberLogin(response, memberLoginRequestDto);
    }

    /** 아이디 찾기 (아이디 확인) **/
    @PostMapping("/member/findid")
    public ResponseEntity<ResponseBody> findMemberEmail(@RequestBody MailDto mailDto){
        log.info("아이디 찾기 메일을 받을 이메일 주소 : {}",
                mailDto.getMailAddress());

        return memberService.findMemberEmail(mailDto);
    }

}
