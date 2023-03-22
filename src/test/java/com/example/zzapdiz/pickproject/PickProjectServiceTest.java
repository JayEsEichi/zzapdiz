package com.example.zzapdiz.pickproject;

import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.exception.pickproject.PickProjectExceptionInterface;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.fundingproject.repository.FundingProjectRepository;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.repository.MemberRepository;
import com.example.zzapdiz.pickproject.repository.PickProjectRepository;
import com.example.zzapdiz.pickproject.service.PickProjectService;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PickProjectServiceTest {

    @InjectMocks
    private PickProjectService pickProjectService;

    @Mock
    private PickProjectRepository pickProjectRepository;

    @Mock
    private FundingProjectRepository fundingProjectRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberExceptionInterface memberExceptionInterface;

    @Mock
    private PickProjectExceptionInterface pickProjectExceptionInterface;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private DynamicQueryDsl dynamicQueryDsl;

    @DisplayName("[PickProjectService] 프로젝트 찜하기 서비스 테스트")
    @Test
    void pickProjectServiceTest(){
        // given
        fundingProjectRepository.save(fakeProject());

        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bHN0cGduczUyQG5hdmVyLmNvbSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjc5NTc3NjU1fQ.WvNmFKTU3Bj8xjLa64fWzXnrFes4q5GljzO1ijFif8U";

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        memberRepository.save(jwtTokenProvider.getMemberFromAuthentication());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", token);
        mockHttpServletRequest.addHeader("Refresh-Token", "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Nzk1Nzc2NTV9.4jnPeM8K0mqjoj70SThCQ8sO2E7rAlALQLsZlBjvN8U");
        // when
        int statusCode = pickProjectService.pickProject(mockHttpServletRequest, 1L).getStatusCodeValue();

        // then
        assertThat(statusCode).isEqualTo(200);

    }

    private FundingProject fakeProject() {
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
