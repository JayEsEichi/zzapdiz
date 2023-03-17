package com.example.zzapdiz.exception.fundingproject;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase3RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase4RequestDto;
import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface FundingProejctExceptionInterface {

    // [잘못된 요청으로 인한 예외 응답 처리]

    /** 생성한 펀딩 프로젝트 1단계 정보 확인 **/
    ResponseEntity<ResponseBody> checkPhase1Info(FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDto);

    /** 생성한 펀딩 프로젝트 2단계 정보 확인 **/
    ResponseEntity<ResponseBody> checkPhase2Info(FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto, MultipartFile thumbnailImage);

    /** 생성한 펀딩 프로젝트 3단계 정보 확인 **/
    ResponseEntity<ResponseBody> checkPhase3Info(FundingProjectCreatePhase3RequestDto fundingProjectCreatePhase3RequestDto, List<MultipartFile> videoAndImages);

    /** 생성한 펀딩 프로젝트 4단계 정보 확인 **/
    ResponseEntity<ResponseBody> checkPhase4Info(FundingProjectCreatePhase4RequestDto fundingProjectCreatePhase4RequestDto);


    // [상황에 따른 예외 응답 처리]

    /** 배송 여부 확인 후 특정배송 시 최소 3000원 배송비 포함, 특정배송이 아닌 기본배송일 경우 배송비 0원 반환 **/
    int deliveryChecking(String deliveryCheck);

    /** 펀딩 프로젝트 생성 마지막 최종 단계에서 반드시 기입되어야할 정보가 하나라도
     * 명확하지 않다면 에러 응답 처리 **/

}
