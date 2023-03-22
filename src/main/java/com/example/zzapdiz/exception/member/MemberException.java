package com.example.zzapdiz.exception.member;

import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.example.zzapdiz.member.domain.QMember.member;

@RequiredArgsConstructor
@Component
public class MemberException implements MemberExceptionInterface{

    private final JPAQueryFactory jpaQueryFactory;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입용 비밀번호와 재확인용 비밀번호 일치여부 확인
    @Override
    public Boolean matchPassword(String password, String passwordRecheck) {
        if(!password.equals(passwordRecheck)){
            return true;
        }
        return false;
    }

    // 이미 가입한 적이 있는 회원인지 확인
    @Override
    public Boolean alreadyExistMember(String email) {
        if(jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne() != null){
            return true;
        }
        return false;
    }

    // 회원가입 정보가 하나라도 비워져있으면 에러 처리
    @Override
    public Boolean allRequestDataCheck(MemberSignupRequestDto memberSignupRequestDto) {
        if(memberSignupRequestDto.getMemberName() == null ||
            memberSignupRequestDto.getPassword() == null ||
            memberSignupRequestDto.getEmail() == null ||
            memberSignupRequestDto.getPasswordRecheck() == null){
            return true;
        }
        return false;
    }

    // 로그인을 시도한 이메일을 가진 계정이 있는지 확인
    @Override
    public Boolean checkEmail(String email) {
        if(jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne() == null){
            return true;
        }

        return false;
    }

    // 중복된 이메일 회원가입 확인
    @Override
    public Boolean duplicatedEmailCheck(String email) {
        if (jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetch() != null){
            return true;
        }

        return false;
    }

    // 비밀번호 일치 여부 확인
    @Override
    public Boolean checkPassword(String password, String existPassword ) {
        if(!passwordEncoder.matches(password, existPassword)){
            return true;
        }

        return false;
    }

    // 유효한 토큰인지 확인
    @Override
    public Boolean checkHeaderToken(HttpServletRequest request) {
        if(!jwtTokenProvider.validateToken(request.getHeader("Refresh-Token"))){
            return true;
        }

        return false;
    }


}
