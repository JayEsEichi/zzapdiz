package com.example.zzapdiz.member;

import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.jwt.repository.TokenRepository;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.repository.MemberRepository;
import com.example.zzapdiz.member.request.MemberLoginRequestDto;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.member.response.MemberSignupResponseDto;
import com.example.zzapdiz.member.service.MemberService;
import com.example.zzapdiz.share.DynamicQueryDsl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DynamicQueryDsl dynamicQueryDsl;

    @Spy
    private MemberExceptionInterface memberExceptionInterface;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @DisplayName("[MemberService] 회원가입 서비스")
    @Test
    void memberSignUpServiceTest() {
        // given
        // 의존 주입받은 회원가입 에러 처리 인터페이스
        memberExceptionInterface.matchPassword(signupRequestDto().getPassword(), signupRequestDto().getPasswordRecheck());
        memberExceptionInterface.alreadyExistMember(signupRequestDto().getEmail());
        memberExceptionInterface.allRequestDataCheck(signupRequestDto());

        // when
        int statusCode = memberService.memberSignUp(signupRequestDto()).getBody().getStatusCode();

        System.out.println("서비스 테스트 ");

        // then
        assertThat(statusCode).isEqualTo(200);
    }


    @DisplayName("[MemberService] 로그인 서비스")
    @Test
    void memberLoginServiceTest(){
        // given
        doReturn(existMember())
                .when(memberRepository)
                .findByEmail(loginRequestDto().getEmail());

        // when
        int statusCode = memberService.memberLogin(any(HttpServletResponse.class), loginRequestDto()).getBody().getStatusCode();

        // then
        assertThat(statusCode).isEqualTo(200);
    }

    private MemberSignupRequestDto signupRequestDto(){
        return MemberSignupRequestDto.builder()
                .email("wlstpgns51@naver.com")
                .memberName("진세훈")
                .password("wls124578!")
                .passwordRecheck("wls124578!")
                .build();
    }

    private MemberSignupResponseDto signupResponseDto(){
        return MemberSignupResponseDto.builder()
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .memberName("진세훈")
                .point(0)
                .build();
    }

    private MemberLoginRequestDto loginRequestDto(){
        return MemberLoginRequestDto.builder()
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .build();
    }

    private Member existMember(){
        return Member.builder()
                .memberId(1L)
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .memberName("진세훈")
                .point(0)
                .build();
    }

    private Member signUpMember(){
        return Member.builder()
                .memberId(1L)
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .memberName("진세훈")
                .point(0)
                .build();
    }
}
