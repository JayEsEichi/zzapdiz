package com.example.zzapdiz.member;

import com.example.zzapdiz.exception.MemberExceptionInterface;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.repository.MemberRepository;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberExceptionInterface memberExceptionInterface;


    @DisplayName("[MemberService] 회원가입 서비스")
    @Test
    void memberSignUpServiceTest() {
        // given
        MemberSignupRequestDto memberSignupRequestDto = MemberSignupRequestDto.builder()
                .email("wlstpgns51@naver.com")
                .memberName("진세훈")
                .password("wls124578!")
                .passwordRecheck("wls124578!")
                .build();

        // 의존 주입받은 회원가입 에러 처리 인터페이스
        memberExceptionInterface.matchPassword(memberSignupRequestDto.getPassword(), memberSignupRequestDto.getPasswordRecheck());
        memberExceptionInterface.alreadyExistMember(memberSignupRequestDto.getEmail());
        memberExceptionInterface.allRequestDataCheck(memberSignupRequestDto);

//        given(memberRepository.save(any()));

        // when
        int statusCode = memberService.memberSignUp(memberSignupRequestDto).getBody().getStatusCode();

        System.out.println("서비스 테스트 ");

        // then
        assertThat(statusCode).isEqualTo(200);
//        verify(memberRepository).save(createMember);
    }

}
