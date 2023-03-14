package com.example.zzapdiz.fundingproject.controller;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.service.FundingProjectService;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/zzapdiz")
@RestController
public class FundingProjectController {

    private final FundingProjectService fundingProjectService;

    /** 펀딩 프로젝트 생성 1단계 **/
    @PostMapping("/funding/create/phase1")
    public ResponseEntity<ResponseBody> fundingCreatePhase1(
            HttpServletRequest request,
            @RequestBody FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDto) {
        log.info("펀딩 프로젝트 생성 1단계 api : 생성자 - {}, 프로젝트 유형 확인 - {}", request, fundingProjectCreatePhase1RequestDto.getProjectType());

        return fundingProjectService.fundingCreatePhase1(request, fundingProjectCreatePhase1RequestDto);
    }

    /** 펀딩 프로젝트 생성 2단계 **/
    @PostMapping(value = "/funding/create/phase2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseBody> fundingCreatePhase2(
            HttpServletRequest request,
            @RequestPart(value="phase2Request", required = false) FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto,
            @RequestPart(value="thumbnailImage", required = false) MultipartFile thumbnailImage){
        log.info("펀딩 프로젝트 생성 2단계 api : 생성자 - {}, 프로젝트 타이틀 확인 - {}", request, fundingProjectCreatePhase2RequestDto.getProjectTitle());

        return fundingProjectService.fundingCreatePhase2(request, fundingProjectCreatePhase2RequestDto, thumbnailImage);
    }

}
