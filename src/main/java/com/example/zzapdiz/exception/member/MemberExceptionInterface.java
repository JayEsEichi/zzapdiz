package com.example.zzapdiz.exception.member;

import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public interface MemberExceptionInterface {

    /** 회원가입 시 입력한 비밀번호와 재확인용 비밀번호 일치 여부 확인 **/
    ResponseEntity<ResponseBody> matchPassword(String password, String passwordRecheck);

    /** 이미 존재한 회원인지 확인 **/
    ResponseEntity<ResponseBody> alreadyExistMember(String email);

    /** 회원가입 정보가 모두 정상적으로 적혀있는지 확인 **/
    ResponseEntity<ResponseBody> allRequestDataCheck(MemberSignupRequestDto memberSignupRequestDto);

    /** 로그인하려고하는 이메일 정보가 존재하지 않을 경우 확인 **/
    ResponseEntity<ResponseBody> checkEmail(String email);

    /** 중복된 이메일로 회원가입할 수 없음 **/
    ResponseEntity<ResponseBody> duplicatedEmailCheck(String email);

    /** 로그인 시 비밃번호가 일치하지 않을 경우 **/
    ResponseEntity<ResponseBody> checkPassword(String password, String existPassword);

    /** 헤더에 발급된 토큰 정보가 유효한지 확인 **/
    ResponseEntity<ResponseBody> checkHeaderToken(HttpServletRequest request);

}
