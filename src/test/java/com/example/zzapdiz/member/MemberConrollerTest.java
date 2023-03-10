package com.example.zzapdiz.member;

import com.example.zzapdiz.member.controller.MemberController;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.member.response.MemberSignupResponseDto;
import com.example.zzapdiz.member.service.MemberService;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.google.gson.Gson;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
public class MemberConrollerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @DisplayName("[MemberController] 회원가입 api")
    @Test
    void memberSignUpTest() throws Exception{
        // given
        MemberSignupRequestDto memberSignupRequestDto = MemberSignupRequestDto.builder()
                .email("wlstpgns51@naver.com")
                .memberName("진세훈")
                .password("wls124578!")
                .passwordRecheck("wls124578!")
                .build();

        MemberSignupResponseDto memberSignupResponseDto = MemberSignupResponseDto.builder()
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .memberName("진세훈")
                .point(0)
                .build();

        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, memberSignupResponseDto), HttpStatus.OK))
                .when(memberService)
                .memberSignUp(any(MemberSignupRequestDto.class));

        String memberSinupInfo = new Gson().toJson(memberSignupRequestDto);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(memberSinupInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(memberSignupRequestDto.getEmail()))
                .andExpect(jsonPath("$.data.memberName").value(memberSignupRequestDto.getMemberName()));

        // then
//        MvcResult mvcResult = (MvcResult) resultActions
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.email").value(memberSignupRequestDto.getEmail()))
//                .andExpect(jsonPath("$.data.memberName").value(memberSignupRequestDto.getMemberName()));

    }
}
