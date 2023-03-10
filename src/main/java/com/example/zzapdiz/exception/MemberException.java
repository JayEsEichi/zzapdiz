package com.example.zzapdiz.exception;

import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.example.zzapdiz.member.domain.QMember.member;

@RequiredArgsConstructor
@Component
public class MemberException implements MemberExceptionInterface{

    private final JPAQueryFactory jpaQueryFactory;

    // 회원가입용 비밀번호와 재확인용 비밀번호 일치여부 확인
    @Override
    public ResponseEntity<ResponseBody> matchPassword(String password, String passwordRecheck) {
        if(!password.equals(passwordRecheck)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.NOT_MATCH_PASSWORD, null), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 이미 가입한 적이 있는 회원인지 확인
    @Override
    public ResponseEntity<ResponseBody> alreadyExistMember(String email) {
        if(jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne() != null){
            return new ResponseEntity<>(new ResponseBody(StatusCode.ALREADY_EXIST_MEMBER, null), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 회원가입 정보가 하나라도 비워져있으면 에러 처리
    @Override
    public ResponseEntity<ResponseBody> allRequestDataCheck(MemberSignupRequestDto memberSignupRequestDto) {
        if(memberSignupRequestDto.getMemberName() == null ||
            memberSignupRequestDto.getPassword() == null ||
            memberSignupRequestDto.getEmail() == null ||
            memberSignupRequestDto.getPasswordRecheck() == null){
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_DATA, null), HttpStatus.BAD_REQUEST);
        }
        return null;
    }


}
