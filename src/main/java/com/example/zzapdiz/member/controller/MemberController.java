package com.example.zzapdiz.member.controller;

import com.example.zzapdiz.member.request.MemberLoginRequestDto;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.member.service.MemberService;
import com.example.zzapdiz.share.mail.MailDto;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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


    /** 로그아웃 **/
    @PostMapping("/member/logout")
    public ResponseEntity<ResponseBody> memberLogout(HttpServletRequest request) throws ServletException {
        log.info("로그아웃 api : 요청 계정 액세스 토큰 - {}", request.getHeader("Authorization"));

        return memberService.memberLogout(request);
    }


    /** 회원탈퇴 **/
    @DeleteMapping("/member/signout")
    public ResponseEntity<ResponseBody> memberSignOut(HttpServletRequest request) throws ServletException {
        log.info("회원탈퇴 api : 요청 계정 - {}", request);

        return memberService.memberSignOut(request);
    }

    /** 아이디 찾기 (아이디 확인) **/
    @PostMapping("/member/findid")
    public ResponseEntity<ResponseBody> findMemberEmail(@RequestBody MailDto mailDto){
        log.info("아이디 찾기 메일을 받을 이메일 주소 : {}",
                mailDto.getMailAddress());

        return memberService.findMemberEmail(mailDto);
    }

}
