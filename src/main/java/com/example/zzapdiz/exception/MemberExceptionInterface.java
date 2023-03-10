package com.example.zzapdiz.exception;

import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface MemberExceptionInterface {

    /** 회원가입 시 입력한 비밀번호와 재확인용 비밀번호 일치 여부 확인 **/
    ResponseEntity<ResponseBody> matchPassword(String password, String passwordRecheck);

    /** 이미 존재한 회원인지 확인 **/
    ResponseEntity<ResponseBody> alreadyExistMember(String email);

    /** 회원가입 정보가 모두 정상적으로 적혀있는지 확인 **/
    ResponseEntity<ResponseBody> allRequestDataCheck(MemberSignupRequestDto memberSignupRequestDto);
}
