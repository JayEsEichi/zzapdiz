package com.example.zzapdiz.supportproject;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.fundingproject.repository.FundingProjectRepository;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.jwt.dto.TokenDto;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.repository.MemberRepository;
import com.example.zzapdiz.supportproject.repository.DoSupportRepository;
import com.example.zzapdiz.supportproject.service.DoSupportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DoSuppportServiceTest {

    @InjectMocks
    private DoSupportService doSupportService;

    @Mock
    private DoSupportRepository doSupportRepository;

    @Mock
    private FundingProjectRepository fundingProjectRepository;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AtomicBoolean atomicBoolean;


    @DisplayName("[DoSupportService] 프로젝트 지지하기 api 서비스 테스트")
    @Test
    void supportProjectserviceTest() throws IOException, ServletException {
        // given
        memberRepository.save(authMember());
        fundingProjectRepository.save(fakeProject(authMember()));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authMember().getEmail(), authMember().getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // Response Header에 액세스 토큰 리프레시 토큰, 토큰 만료일 input
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        mockHttpServletResponse.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        mockHttpServletResponse.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        mockHttpServletResponse.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        String token = tokenDto.getAccessToken();

        authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.authenticate(mockHttpServletResponse);
//        mockHttpServletRequest.addHeader("Authirization", token);
//        mockHttpServletRequest.addHeader("Refresh-Token", "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Nzk1Nzc2NTV9.4jnPeM8K0mqjoj70SThCQ8sO2E7rAlALQLsZlBjvN8U");


        // when
        int statusCode = doSupportService.supportProject(mockHttpServletRequest, 1L).getStatusCodeValue();

        // then
        assertThat(statusCode).isEqualTo(200);

    }

    private FundingProject fakeProject(Member authMember) {
        return FundingProject.builder()
                .fundingProjectId(1L)
                .projectTitle("찜하기 테스트용 프로젝트명")
                .projectCategory("전자/가전")
                .projectType("손수 제작")
                .achievedAmount(90000000)
                .adultCheck("X")
                .searchTag("#검색태그")
                .storyText("프로젝트 스토리~~")
                .projectDescript("프로젝트 요약 설명~~")
                .openReservation("X")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .makerType("개인")
                .progress("진행중")
                .deliveryCheck("X")
                .member(authMember)
                .build();
    }


    private Member authMember(){
        return Member.builder()
                .memberId(1L)
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .memberName("rltrlt")
                .point(0)
                .build();
    }
}
