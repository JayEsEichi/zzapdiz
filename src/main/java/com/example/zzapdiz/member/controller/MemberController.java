package com.example.zzapdiz.member.controller;

import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.member.service.MemberService;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
}
