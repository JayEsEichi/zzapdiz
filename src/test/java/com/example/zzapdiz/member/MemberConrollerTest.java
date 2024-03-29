package com.example.zzapdiz.member;

import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.controller.MemberController;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.request.MemberLoginRequestDto;
import com.example.zzapdiz.member.request.MemberSignupRequestDto;
import com.example.zzapdiz.member.response.MemberSignupResponseDto;
import com.example.zzapdiz.member.service.MemberService;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberConrollerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private DynamicQueryDsl dynamicQueryDsl;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @DisplayName("[MemberController] 회원가입 api")
    @Test
    void memberSignUpTest() throws Exception {
        // given
        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, signupResponseDto()), HttpStatus.OK))
                .when(memberService)
                .memberSignUp(any(MemberSignupRequestDto.class));

        String memberSinupInfo = new Gson().toJson(signupRequestDto());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(memberSinupInfo));

        // then
        ResultActions resultActionsThen = resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(signupRequestDto().getEmail()))
                .andExpect(jsonPath("memberName").value(signupRequestDto().getMemberName()));

    }


    @DisplayName("[MemberController] 로그인 api")
    @Test
    void memberLoginTest() throws Exception {
        // given
        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, existMember()), HttpStatus.OK))
                .when(memberService)
                .memberLogin(any(HttpServletResponse.class), any(MemberLoginRequestDto.class));

        String memberLoginInfo = new Gson().toJson(loginRequestDto());

        // when
        ResultActions resultActionsWhen = mockMvc.perform(
                MockMvcRequestBuilders.get("/zzapdiz/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(memberLoginInfo));

        // then
        ResultActions resultActionsThen = resultActionsWhen
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(loginRequestDto().getEmail()))
                .andExpect(jsonPath("$.data.password").value(loginRequestDto().getPassword()));
    }

    @DisplayName("[MemberController] 로그아웃 api")
    @Test
    void memberLogoutTest() throws Exception {
        // given
        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, "정상적으로 로그아웃 되셨습니다. 이용해주셔서 감사합니다 ^^"), HttpStatus.OK))
                .when(memberService)
                .memberLogout(any(MockHttpServletRequest.class));

        // when
        ResultActions resultActionsWhen = mockMvc.perform(
                MockMvcRequestBuilders.post("/zzapdiz/member/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"));

        // then
        ResultActions resultActionsThen = resultActionsWhen
                .andDo(print())
                .andExpect(status().isOk());

        verify(memberService).memberLogout(any(MockHttpServletRequest.class));
    }

    @DisplayName("[MemberController] 회원탈퇴 api")
    @Test
    void memberSignOutTest() throws Exception{
        // given
        memberService.memberSignUp(signupRequestDto());

        doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, "정상적으로 회원탈퇴되었습니다. 다음에 다시 만나뵙기를 기대하고 있겠습니다."), HttpStatus.OK))
                .when(memberService)
                .memberSignOut(any(MockHttpServletRequest.class));

        // when
        ResultActions resultActionsWhen = mockMvc.perform(
                MockMvcRequestBuilders.delete("/zzapdiz/member/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());

    }


    private MemberSignupRequestDto signupRequestDto() {
        return MemberSignupRequestDto.builder()
                .email("wlstpgns51@naver.com")
                .memberName("진세훈")
                .password("wls124578!")
                .passwordRecheck("wls124578!")
                .build();
    }

    private MemberSignupResponseDto signupResponseDto() {
        return MemberSignupResponseDto.builder()
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .memberName("진세훈")
                .point(0)
                .build();
    }

    private MemberLoginRequestDto loginRequestDto() {
        return MemberLoginRequestDto.builder()
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .build();
    }

    private Member existMember() {
        return Member.builder()
                .memberId(1L)
                .email("wlstpgns51@naver.com")
                .password("wls124578!")
                .memberName("진세훈")
                .point(0)
                .build();
    }
}
