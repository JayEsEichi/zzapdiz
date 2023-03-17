package com.example.zzapdiz.fundingproject.controller;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase3RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase4RequestDto;
import com.example.zzapdiz.fundingproject.service.FundingProjectService;
import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /** 펀딩 프로젝트 생성 3단계 **/
    @PostMapping("/funding/create/phase3")
    public ResponseEntity<ResponseBody> fundingCreatePhase3(
            HttpServletRequest request,
            @RequestPart(value="phase3Request", required = false)FundingProjectCreatePhase3RequestDto fundingProjectCreatePhase3RequestDto,
            @RequestPart(value="videoAndImages") List<MultipartFile> videoAndImages){
        log.info("펀딩 프로젝트 생성 3단계 api : 생성자 - {}, 프로젝트 파일 첨부 확인 - {}", request, videoAndImages.get(0));

        return fundingProjectService.fundingCreatePhase3(request, fundingProjectCreatePhase3RequestDto, videoAndImages);
    }

    /** 펀딩 프로젝트 생성 4단계 **/
    @PostMapping(value = "/funding/create/phase4", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseBody> fundingCreatePhase4(
            HttpServletRequest request,
            @RequestPart(value = "phase4RequestDto", required = false) FundingProjectCreatePhase4RequestDto fundingProjectCreatePhase4RequestDto,
            @RequestPart(value = "rewardRequestInfo", required = false) List<RewardCreateRequestDto> rewardCreateRequestDtos){
        log.info("펀딩 프로젝트 생성 4단계 api : 생성자 - {}, 프로젝트 특정배송 체크 - {}", request, fundingProjectCreatePhase4RequestDto.getDeliveryCheck());

        return fundingProjectService.fundingCreatePhase4(request, fundingProjectCreatePhase4RequestDto, rewardCreateRequestDtos);
    }

}
