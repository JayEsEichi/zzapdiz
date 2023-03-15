package com.example.zzapdiz.exception.fundingproject;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase3RequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FundingProjectException implements FundingProejctExceptionInterface {

    // 펀딩 프로젝트 생성 1단계 기입 정보들 확인
    @Override
    public ResponseEntity<ResponseBody> checkPhase1Info(FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDto) {
        if(fundingProjectCreatePhase1RequestDto.getProjectCategory() == null ||
        fundingProjectCreatePhase1RequestDto.getProjectType() == null ||
        fundingProjectCreatePhase1RequestDto.getMakerType() == null ||
        fundingProjectCreatePhase1RequestDto.getRewardType() == null ||
        fundingProjectCreatePhase1RequestDto.getRewardMakeType() == null ||
        fundingProjectCreatePhase1RequestDto.getAchievedAmount() == 0){
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    // 펀딩 프로젝트 생성 2단계 기입 정보들 확인
    @Override
    public ResponseEntity<ResponseBody> checkPhase2Info(FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto, MultipartFile thumbnailImage) {
        if (fundingProjectCreatePhase2RequestDto.getProjectTitle() == null ||
                fundingProjectCreatePhase2RequestDto.getAdultCheck() == null ||
                fundingProjectCreatePhase2RequestDto.getEndDate() == null ||
                fundingProjectCreatePhase2RequestDto.getSearchTag() == null ||
                thumbnailImage == null) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 펀딩 프로젝트 생성 3단계 기입 정보들 확인
    @Override
    public ResponseEntity<ResponseBody> checkPhase3Info(FundingProjectCreatePhase3RequestDto fundingProjectCreatePhase3RequestDto, List<MultipartFile> videoAndImages) {
        if (fundingProjectCreatePhase3RequestDto.getProjectDescript() == null ||
                fundingProjectCreatePhase3RequestDto.getOpenReservation() == null ||
                fundingProjectCreatePhase3RequestDto.getStoryText() == null ||
                videoAndImages.isEmpty()) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }

        return null;
    }
}
