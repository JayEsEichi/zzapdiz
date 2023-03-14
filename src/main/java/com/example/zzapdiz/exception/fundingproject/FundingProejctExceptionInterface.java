package com.example.zzapdiz.exception.fundingproject;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.share.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface FundingProejctExceptionInterface {

    /** 생성한 펀딩 프로젝트 2단계 정보 확인 **/
    ResponseEntity<ResponseBody> checkPhase2Info(FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto, MultipartFile thumbnailImage);

    /** 펀딩 프로젝트 생성 마지막 최종 단계에서 반드시 기입되어야할 정보가 하나라도
     * 명확하지 않다면 에러 응답 처리 **/

}
