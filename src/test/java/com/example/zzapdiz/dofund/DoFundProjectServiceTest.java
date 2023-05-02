package com.example.zzapdiz.dofund;

import com.example.zzapdiz.dofundproject.repository.DoFundPhase1Repository;
import com.example.zzapdiz.dofundproject.repository.DoFundPhase2Repository;
import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.example.zzapdiz.dofundproject.request.DoFundPhase2RequestDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase1ResponseDto;
import com.example.zzapdiz.dofundproject.service.DoFundService;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.fundingproject.repository.FundingProjectRepository;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase4RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingRewardUpdateRequestDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase2ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase4ResponseDto;
import com.example.zzapdiz.fundingproject.service.FundingProjectService;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.repository.MemberRepository;
import com.example.zzapdiz.member.request.MemberLoginRequestDto;
import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.reward.response.RewardCreateResponseDto;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class DoFundProjectServiceTest {

    @InjectMocks
    private DoFundService doFundService;

    @Mock
    private DoFundPhase1Repository doFundPhase1Repository;

    @Mock
    private DoFundPhase2Repository doFundPhase2Repository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @DisplayName("[DoFundService] 펀딩하기 1단계 서비스")
    @Test
    void createFundingPhase1() {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        List<DoFundPhase1RequestDto> phase1List = new ArrayList<>();
        phase1List.add(getPhase1Request(1L));
        phase1List.add(getPhase1Request(2L));

        // when
        int statusCode = doFundService.doFundPhase1(mockHttpServletRequest, phase1List).getBody().getStatusCode();

        // then
        assertThat(statusCode).isEqualTo(200);
//        assertThat(fundingProjectCreatePhase1RequestDto.getProjectType()).isEqualTo(phase1RequestDto().getProjectType());
    }

    @DisplayName("[DoFundService] 펀딩 프로젝트 생성 2단계 서비스")
    @Test
    void createFundingPhase2() throws Exception{
        // given
        HttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        HttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        mockHttpServletResponse.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bHN0cGduczUyQG5hdmVyLmNvbSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjgzMDg3MjI0fQ.GIgtrWM3soTMojMZfXSHwPUQ3dXK-J1a44vGlBpbt14");

        mockHttpServletRequest.authenticate(mockHttpServletResponse);

        // when
        int statusCode = doFundService.doFundPhase2(mockHttpServletRequest, getPhase2Request()).getBody().getStatusCode();

        // then
        assertThat(statusCode).isEqualTo(200);
    }

    private DoFundPhase1RequestDto getPhase1Request(Long rewardId){
        return DoFundPhase1RequestDto.builder()
                .fundingProjectId(1L)
                .rewardId(rewardId)
                .build();
    }

    private DoFundPhase2RequestDto getPhase2Request(){
        return DoFundPhase2RequestDto.builder()
                .address("강서구")
                .phoneNumber("01022225555")
                .couponId(null)
                .point(null)
                .donation(null)
                .build();
    }

}
