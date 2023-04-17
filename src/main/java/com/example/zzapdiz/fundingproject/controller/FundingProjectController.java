package com.example.zzapdiz.fundingproject.controller;

import com.example.zzapdiz.fundingproject.request.*;
import com.example.zzapdiz.fundingproject.response.ProjectsReadResponseDto;
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

    /** 펀딩 프로젝트 생성 5단계 마지막 **/
    @PostMapping("/funding/create/phase5")
    public ResponseEntity<ResponseBody> fundingCreateFinal(HttpServletRequest request){
        log.info("펀딩 프로젝트 생성 5단계 마지막 api : 생성자 - {}", request);

        return fundingProjectService.fundingCreateFinal(request);
    }

    /** 생성 중인 리워드 수정 **/
    @PostMapping("/funding/reward/update")
    public ResponseEntity<ResponseBody> rewardUpdate(HttpServletRequest request, @RequestBody FundingRewardUpdateRequestDto fundingRewardUpdateRequestDto){
        log.info("리워드 수정 api : 요청자 - {}, 리워드 수정 확인 제목 - {}", request, fundingRewardUpdateRequestDto.getRewardTitle());

        return fundingProjectService.rewardUpdate(request, fundingRewardUpdateRequestDto);
    }

    /** 생성 중인 리워드 삭제 **/
    @DeleteMapping("/funding/reward/delete/{rewardNo}")
    public ResponseEntity<ResponseBody> rewardDelete(HttpServletRequest request, @PathVariable int rewardNo){
        log.info("리워드 삭제 api : 요청자 - {}, 리워드 삭제 리워드 - {}", request, rewardNo);

        return fundingProjectService.rewardDelete(request, rewardNo);
    }

    /** 펀딩 프로젝트 조회 **/
    @GetMapping("/funding/read/{projectId}")
    public ResponseEntity<ResponseBody> fundingProjectRead(@PathVariable Long projectId){
        log.info("펀딩 프로젝트 조회 api : 프로젝트 - {}", projectId);

        return fundingProjectService.fundingProjectRead(projectId);
    }

    /**
     * 펀딩 프로젝트 목록 조회
     * (1) 프로젝트 카테고리 / (2) 진행상황 / (3) 정렬기준 / (4) 12개씩 페이지 번호
     * **/
    @GetMapping("/funding/readlist")
    public ResponseEntity<ResponseBody> fundingProjectsRead(@RequestBody ProjectsListReadRequestDto projectsListReadRequestDto){
        log.info("펀딩 프로젝트 목록 조회 api : 카테고리 - {}, 진행상황 - {}, 정렬기준 - {}, 목록 번호 - {}",
                projectsListReadRequestDto.getProjectCategory(), projectsListReadRequestDto.getProgress(), projectsListReadRequestDto.getOrderBy(), projectsListReadRequestDto.getPageNum());

        return fundingProjectService.fundingProjectsRead(projectsListReadRequestDto);
    }

    /** 펀딩 프로젝트 삭제 **/
    @DeleteMapping("/funding/delete/{projectId}")
    public ResponseEntity<ResponseBody> fundingProjectDelete(HttpServletRequest request, @PathVariable Long projectId){
        log.info("펀딩 프로젝트 삭제 api : 삭제 유저 - {}, 삭제될 프로젝트 id - {}", request, projectId);

        return fundingProjectService.fundingProjectDelete(request, projectId);
    }
}
