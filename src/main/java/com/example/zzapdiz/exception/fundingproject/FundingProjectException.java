package com.example.zzapdiz.exception.fundingproject;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FundingProjectException implements  FundingProejctExceptionInterface{

    @Override
    public ResponseEntity<ResponseBody> checkPhase2Info(FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto, MultipartFile thumbnailImage) {
        if(fundingProjectCreatePhase2RequestDto.getProjectTitle() == null ||
        fundingProjectCreatePhase2RequestDto.getAdultCheck() == null ||
        fundingProjectCreatePhase2RequestDto.getEndDate() == null ||
        fundingProjectCreatePhase2RequestDto.getSearchTag() == null ||
        thumbnailImage == null){
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
