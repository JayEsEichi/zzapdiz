package com.example.zzapdiz.fundingproject;

import com.example.zzapdiz.exception.member.MemberException;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.service.FundingProjectService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundingProjectServiceTest {

    @InjectMocks
    private FundingProjectService fundingProjectService;

    @Mock
    private MemberException memberException;

    @DisplayName("[FundingProjectService] 펀딩 프로젝트 생성 1단계 서비스")
    @Test
    void createFundingPhase1() {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        // when
        int statusCode = fundingProjectService.fundingCreatePhase1(
                mockHttpServletRequest,
                phase1RequestDto()).getBody().getStatusCode();

        String phase1Info = new Gson().toJson(mockHttpServletRequest.getAttribute("phase1Info"));
        FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDto =
                new Gson().fromJson(phase1Info, FundingProjectCreatePhase1RequestDto.class);

        // then
        assertThat(statusCode).isEqualTo(200);
        assertThat(fundingProjectCreatePhase1RequestDto.getProjectType()).isEqualTo(phase1RequestDto().getProjectType());
    }

    private FundingProjectCreatePhase1RequestDto phase1RequestDto(){
        return FundingProjectCreatePhase1RequestDto.builder()
                .projectCategory("TECH")
                .projectType("FIRST_OPEN")
                .makerType("PERSONAL!")
                .rewardType("가구/인테리어")
                .rewardMakeType("SELF_MADE")
                .achievedAmount(8000000)
                .build();
    }

}
