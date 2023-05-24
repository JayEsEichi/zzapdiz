//package com.example.zzapdiz.supportproject;
//
//import com.example.zzapdiz.fundingproject.domain.FundingProject;
//import com.example.zzapdiz.share.ResponseBody;
//import com.example.zzapdiz.share.StatusCode;
//import com.example.zzapdiz.supportproject.controller.DoSupportController;
//import com.example.zzapdiz.supportproject.service.DoSupportService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//public class DoSupportControllerTest {
//
//    @InjectMocks
//    private DoSupportController doSupportController;
//
//    @Mock
//    private DoSupportService doSupportService;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(doSupportController).build();
//    }
//
//    @DisplayName("[DoSupportController] 프로젝트 지지하기 api 컨트롤러 테스트")
//    @Test
//    void supportProjectTest() throws Exception{
//        // given
//        Mockito.doReturn(new ResponseEntity<>(new ResponseBody(StatusCode.OK, fakeProject()), HttpStatus.OK))
//                .when(doSupportService)
//                .supportProject(any(MockHttpServletRequest.class), any(Long.class));
//
//        // when
//        ResultActions resultActionsWhen = mockMvc.perform(
//                MockMvcRequestBuilders.post("/zzapdiz/project/support/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3bHN0cGduczUyQG5hdmVyLmNvbSIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjc5NTc3NjU1fQ.WvNmFKTU3Bj8xjLa64fWzXnrFes4q5GljzO1ijFif8U")
//                        .characterEncoding("utf-8")
//        );
//
//        // then
//        resultActionsWhen
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    private FundingProject fakeProject() {
//        return FundingProject.builder()
//                .fundingProjectId(1L)
//                .projectTitle("지지하기 테스트용 프로젝트명")
//                .projectCategory("전자/가전")
//                .projectType("손수 제작")
//                .achievedAmount(90000000)
//                .adultCheck("X")
//                .searchTag("#검색태그")
//                .storyText("프로젝트 스토리~~")
//                .projectDescript("프로젝트 요약 설명~~")
//                .openReservation("X")
//                .startDate(LocalDateTime.now())
//                .endDate(LocalDateTime.now())
//                .makerType("개인")
//                .progress("진행중")
//                .deliveryCheck("X")
//                .build();
//    }
//}
