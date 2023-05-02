package com.example.zzapdiz.dofund;

import com.example.zzapdiz.dofundproject.controller.DoFundController;
import com.example.zzapdiz.dofundproject.repository.DoFundPhase1Repository;
import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.example.zzapdiz.dofundproject.request.DoFundPhase2RequestDto;
import com.example.zzapdiz.dofundproject.service.DoFundService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DoFundProjectControllerTest {

    @InjectMocks
    private DoFundController doFundController;

    @Mock
    private DoFundService doFundService;

    @Mock
    private DoFundPhase1Repository doFundPhase1Repository;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(doFundController).build();
    }

    @DisplayName("[DoFundProjectController] 펀딩하기 1단계 api")
    @Test
    void createFundingPhase1() throws Exception {
        // given
        String fakeData = new Gson().toJson(getPhase1Request(1L));

        List<DoFundPhase1RequestDto> phase1InfoList = new ArrayList<>();
        phase1InfoList.add(getPhase1Request(1L));
        phase1InfoList.add(getPhase1Request(2L));

        String phase1RequestDtos = new Gson().toJson(phase1InfoList);
        System.out.println(phase1RequestDtos);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("resultMessage", "펀딩하기 1단계 성공");
        resultSet.put("resultData", phase1InfoList);

        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK))
                .when(doFundService)
                .doFundPhase1(any(MockHttpServletRequest.class), anyList());

        // when
        ResultActions resultActionsWhen = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/dofund/phase1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bHN0cGduczUyQG5hdmVyLmNvbSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjgzMDg3MjI0fQ.GIgtrWM3soTMojMZfXSHwPUQ3dXK-J1a44vGlBpbt14")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(fakeData));

        // then
        ResultActions resultActionsThen = resultActionsWhen
                .andDo(print())
                .andExpect(status().isOk());

    }


    @DisplayName("[DoFundProjectController] 펀딩하기 2단계 api")
    @Test
    void createFundingPhase2() throws Exception {
        // given
        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("resultMessage", "펀딩하기 2단계 성공");
        resultSet.put("resultData", getPhase2Request());

        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK))
                .when(doFundService)
                .doFundPhase2(any(MockHttpServletRequest.class), any(DoFundPhase2RequestDto.class));

        String phase2Info = new Gson().toJson(getPhase2Request());

        // when
        ResultActions resultActionsWhen = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/dofund/phase2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bHN0cGduczUyQG5hdmVyLmNvbSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjgzMDg3MjI0fQ.GIgtrWM3soTMojMZfXSHwPUQ3dXK-J1a44vGlBpbt14")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(phase2Info));

        // then
        ResultActions resultActionsThen = resultActionsWhen
                .andDo(print())
                .andExpect(status().isOk());

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
