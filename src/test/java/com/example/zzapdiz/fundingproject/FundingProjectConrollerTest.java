package com.example.zzapdiz.fundingproject;

import com.example.zzapdiz.fundingproject.controller.FundingProjectController;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase1ResponseDto;
import com.example.zzapdiz.fundingproject.service.FundingProjectService;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FundingProjectConrollerTest {

    @InjectMocks
    private FundingProjectController fundingProjectController;

    @Mock
    private FundingProjectService fundingProjectService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(fundingProjectController).build();
    }

    @DisplayName("[FundingProjectController] 펀딩 프로젝트 생성 1단계 api")
    @Test
    void createFundingPhase1() throws Exception {
        // given
        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, phase1ResponseDto()), HttpStatus.OK))
                .when(fundingProjectService)
                .fundingCreatePhase1(any(MockHttpServletRequest.class), any(FundingProjectCreatePhase1RequestDto.class));

        String phase1RequestDto = new Gson().toJson(phase1RequestDto());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/funding/create/phase1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(phase1RequestDto));

        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.projectCategory").value(phase1RequestDto().getProjectCategory()))
                .andExpect(jsonPath("$.data.projectType").value(phase1RequestDto().getProjectType()))
                .andExpect(jsonPath("$.data.makerType").value(phase1RequestDto().getMakerType()))
                .andExpect(jsonPath("$.data.rewardType").value(phase1RequestDto().getRewardType()))
                .andExpect(jsonPath("$.data.rewardMakeType").value(phase1RequestDto().getRewardMakeType()))
                .andExpect(jsonPath("$.data.achievedAmount").value(phase1RequestDto().getAchievedAmount()));

    }

    private FundingProjectCreatePhase1RequestDto phase1RequestDto() {
        return FundingProjectCreatePhase1RequestDto.builder()
                .projectCategory("TECH")
                .projectType("FIRST_OPEN")
                .makerType("PERSONAL!")
                .rewardType("가구/인테리어")
                .rewardMakeType("SELF_MADE")
                .achievedAmount(8000000)
                .build();
    }

    private FundingProjectCreatePhase1ResponseDto phase1ResponseDto(){
        return FundingProjectCreatePhase1ResponseDto.builder()
                .projectCategory("TECH")
                .projectType("FIRST_OPEN")
                .makerType("PERSONAL!")
                .rewardType("가구/인테리어")
                .rewardMakeType("SELF_MADE")
                .achievedAmount(8000000)
                .build();
    }
}
