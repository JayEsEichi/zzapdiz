package com.example.zzapdiz.fundingproject.controller;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.service.FundingProjectService;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/zzapdiz")
@RestController
public class FundingProjectController {

    private final FundingProjectService fundingProjectService;

    /**
     * 펀딩 프로젝트 생성 1단계
     **/
    @PostMapping("/funding/create/phase1")
    public ResponseEntity<ResponseBody> fundingCreatePhase1(
            HttpServletRequest request,
            @RequestBody FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDto) {
        log.info("펀딩 프로젝트 생성 1단계 api : 생성자 - {}, 프로젝트 유형 - {}", request, fundingProjectCreatePhase1RequestDto.getProjectType());

        return fundingProjectService.fundingCreatePhase1(request, fundingProjectCreatePhase1RequestDto);
    }

}
