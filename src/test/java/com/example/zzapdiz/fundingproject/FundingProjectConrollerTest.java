package com.example.zzapdiz.fundingproject;

import com.example.zzapdiz.fundingproject.controller.FundingProjectController;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.fundingproject.request.*;
import com.example.zzapdiz.fundingproject.response.*;
import com.example.zzapdiz.fundingproject.service.FundingProjectService;
import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.reward.response.RewardCreateResponseDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.project.MakerType;
import com.example.zzapdiz.share.project.ProjectCategory;
import com.example.zzapdiz.share.project.ProjectType;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
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

    @DisplayName("[FundingProjectController] 펀딩 프로젝트 생성 2단계 api")
    @Test
    void createFundingPhase2() throws Exception {
        // given
        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, phase2ResponseDto()), HttpStatus.OK))
                .when(fundingProjectService)
                .fundingCreatePhase2(any(MockHttpServletRequest.class), any(FundingProjectCreatePhase2RequestDto.class), any(MockMultipartFile.class));

        String phase2RequestDto = new Gson().toJson(phase2RequestDto());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/funding/create/phase2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding("utf-8")
                        .content(phase2RequestDto));
        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectTitle").value(phase2RequestDto().getProjectTitle()));

    }

    @DisplayName("[FundingProjectController] 펀딩 프로젝트 생성 3단계 api")
    @Test
    void createFundingPhase3() throws Exception {
        // given
        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, phase3ResponseDto()), HttpStatus.OK))
                .when(fundingProjectService)
                .fundingCreatePhase3(any(MockHttpServletRequest.class), any(FundingProjectCreatePhase3RequestDto.class), anyList());

        String phase3RequestDto = new Gson().toJson(phase3RequestDto());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/funding/create/phase3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding("utf-8")
                        .content(phase3RequestDto));
        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phase3Info.storyText").value(phase3RequestDto().getStoryText()));

    }


    @DisplayName("[FundingProjectController] 펀딩 프로젝트 생성 4단계 api")
    @Test
    void createFundingPhase4() throws Exception {
        // given
        List<RewardCreateResponseDto> rewardCreateResponseDtos = new ArrayList<>();
        rewardCreateResponseDtos.add(rewardCreateResponseDto());

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("phase4Info", phase4ResponseDto());
        resultSet.put("rewardInfo", rewardCreateResponseDtos);

        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK))
                .when(fundingProjectService)
                .fundingCreatePhase4(any(MockHttpServletRequest.class), any(FundingProjectCreatePhase4RequestDto.class), anyList());

        String phase4RequestDto = new Gson().toJson(phase4RequestDto());
        String rewardCreateRequestDto = new Gson().toJson(rewardCreateRequestDto());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/funding/create/phase4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8")
                        .content(phase4RequestDto)
                        .content(rewardCreateRequestDto));
        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.data.phase3Info.storyText").value(phase3RequestDto().getStoryText()));

    }


    @DisplayName("[FundingProjectController] 펀딩 프로젝트 생성 5단계 api")
    @Test
    void createFundingPhas5() throws Exception {
        // given
        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, "펀딩 프로젝트 마지막 생성 완료"), HttpStatus.OK))
                .when(fundingProjectService)
                .fundingCreateFinal(any(MockHttpServletRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/funding/create/phase5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("utf-8"));

        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("[FundingProjectController] 생성중인 리워드 수정 api")
    @Test
    void rewardUpdate() throws Exception {
        // given
        RewardCreateResponseDto rewardCreateResponseDto = updateReward(fundingRewardUpdateRequestDto());

        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, rewardCreateResponseDto), HttpStatus.OK))
                .when(fundingProjectService)
                .rewardUpdate(any(MockHttpServletRequest.class), any(FundingRewardUpdateRequestDto.class));

        String rewardUpdate = new Gson().toJson(fundingRewardUpdateRequestDto());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/funding/reward/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bHN0cGduczUyQG5hdmVyLmNvbSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjc5MzkwOTI3fQ.pARIMdKLC_MmxYWYKW25eJe2aDEAfnvqpy17aiOKXMc")
                        .characterEncoding("utf-8")
                        .content(rewardUpdate));

        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rewardTitle").value(fundingRewardUpdateRequestDto().getRewardTitle()));

    }


    @DisplayName("[FundingProjectController] 생성중인 리워드 삭제 api")
    @Test
    void rewardDelete() throws Exception {
        // given
        int no = 0;

        RewardCreateResponseDto rewardCreateResponseDto = updateReward(fundingRewardUpdateRequestDto());

        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, rewardCreateResponseDto), HttpStatus.OK))
                .when(fundingProjectService)
                .rewardDelete(any(MockHttpServletRequest.class), any(int.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/zzapdiz/funding/reward/delete/" + no)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bHN0cGduczUyQG5hdmVyLmNvbSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjc5MzkwOTI3fQ.pARIMdKLC_MmxYWYKW25eJe2aDEAfnvqpy17aiOKXMc")
                        .characterEncoding("utf-8"));

        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("[FundingProjectController] 펀딩 프로젝트 조회")
    @Test
    void readFundingProject() throws Exception {
        // given
        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("resultMessage", "조회 테스트 성공");
        resultSet.put("resultInfo", getReadResponse());

        doReturn(new ResponseEntity<>(new ResponseBody<>(StatusCode.OK, resultSet), HttpStatus.OK))
                .when(fundingProjectService)
                .fundingProjectRead(1L);

        // when
        ResultActions resultActionsWhen = mockMvc.perform(
                MockMvcRequestBuilders.get("/zzapdiz/funding/read/1")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        ResultActions resultActionsThen = resultActionsWhen
                .andDo(print())
                .andExpect(status().isOk());

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

    private FundingProjectCreatePhase1ResponseDto phase1ResponseDto() {
        return FundingProjectCreatePhase1ResponseDto.builder()
                .projectCategory("TECH")
                .projectType("FIRST_OPEN")
                .makerType("PERSONAL!")
                .rewardType("가구/인테리어")
                .rewardMakeType("SELF_MADE")
                .achievedAmount(8000000)
                .build();
    }

    private FundingProjectCreatePhase2RequestDto phase2RequestDto() {
        return FundingProjectCreatePhase2RequestDto.builder()
                .projectTitle("생성 2단계 프로젝트 타이틀")
                .endDate("20230314")
                .adultCheck("O")
                .searchTag("#검색태그1-#검색태그2")
                .build();
    }

    private FundingProjectCreatePhase2ResponseDto phase2ResponseDto() {
        return FundingProjectCreatePhase2ResponseDto.builder()
                .projectTitle("생성 2단계 프로젝트 타이틀")
                .endDate("20230314")
                .adultCheck("O")
                .searchTag("#검색태그1-#검색태그2")
                .build();
    }

    private FundingProjectCreatePhase3RequestDto phase3RequestDto() {
        return FundingProjectCreatePhase3RequestDto.builder()
                .storyText("프로젝트 소개글 완성")
                .projectDescript("프로젝트 요약 설명")
                .openReservation("O")
                .build();
    }

    private FundingProjectCreatePhase3ResponseDto phase3ResponseDto() {
        return FundingProjectCreatePhase3ResponseDto.builder()
                .memberId(1L)
                .storyText("프로젝트 소개글 완성")
                .projectDescript("프로젝트 요약 설명 완성")
                .openReservation("O")
                .build();
    }

    private FundingProjectCreatePhase4RequestDto phase4RequestDto() {
        return FundingProjectCreatePhase4RequestDto.builder()
                .deliveryCheck("O")
                .deliveryStartDate("20230316")
                .build();
    }

    private FundingProjectCreatePhase4ResponseDto phase4ResponseDto() {
        return FundingProjectCreatePhase4ResponseDto.builder()
                .memberId(1L)
                .deliveryCheck("O")
                .deliveryPrice(3000)
                .deliveryStartDate("20230316")
                .build();
    }

    private RewardCreateRequestDto rewardCreateRequestDto() {
        return RewardCreateRequestDto.builder()
                .rewardTitle("리워드")
                .rewardContent("초회 한정 리워드")
                .rewardQuantity(67000)
                .rewardAmount(60)
                .rewardOptionOnOff("O")
                .optionContent("색상은 빨강")
                .build();
    }

    private RewardCreateResponseDto rewardCreateResponseDto() {
        return RewardCreateResponseDto.builder()
                .memberId(1L)
                .rewardTitle("리워드")
                .rewardContent("초회 한정 리워드")
                .rewardQuantity(67000)
                .rewardAmount(60)
                .rewardOptionOnOff("O")
                .optionContent("색상은 빨강")
                .build();
    }

    private FundingRewardUpdateRequestDto fundingRewardUpdateRequestDto() {
        return FundingRewardUpdateRequestDto.builder()
                .no(0)
                .rewardContent("리워드 수정 테스트")
                .rewardTitle("리워드 수정 타이틀")
                .rewardQuantity(80000)
                .rewardAmount(90)
                .build();
    }

    private RewardCreateResponseDto updateReward(FundingRewardUpdateRequestDto fundingRewardUpdateRequestDto) {
        return RewardCreateResponseDto.builder()
                .memberId(2L)
                .rewardQuantity(fundingRewardUpdateRequestDto.getRewardQuantity())
                .rewardTitle(fundingRewardUpdateRequestDto.getRewardTitle())
                .rewardContent(fundingRewardUpdateRequestDto.getRewardContent())
                .rewardAmount(fundingRewardUpdateRequestDto.getRewardAmount())
                .no(fundingRewardUpdateRequestDto.getNo())
                .build();
    }

    private ProjectReadResponseDto getReadResponse() {
        FundingProject fakeProject = FundingProject.builder()
                .fundingProjectId(1L)
                .projectCategory("dd")
                .projectType("dd")
                .makerType("dd")
                .achievedAmount(900)
                .projectTitle("거짓 타이틀")
                .endDate(LocalDateTime.now())
                .adultCheck("X")
                .searchTag("hh")
                .storyText("kk")
                .projectDescript("ll")
                .openReservation("X")
                .startDate(LocalDateTime.now())
                .deliveryCheck("X")
                .deliveryPrice(90000)
                .deliveryStartDate(LocalDateTime.now())
                .progress("진행중")
                .build();

        return ProjectReadResponseDto.builder()
                .fundingProject(fakeProject)
                .build();
    }

}
