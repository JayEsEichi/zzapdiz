package com.example.zzapdiz.exception.fundingproject;

import com.example.zzapdiz.fundingproject.request.*;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase1ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase2ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase3ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase4ResponseDto;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.share.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Component
public interface FundingProejctExceptionInterface {

    // [잘못된 요청으로 인한 예외 응답 처리]

    /** 생성한 펀딩 프로젝트 1단계 정보 확인 **/
    Boolean checkPhase1Info(FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDto);

    /** 생성한 펀딩 프로젝트 2단계 정보 확인 **/
    Boolean checkPhase2Info(FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto, MultipartFile thumbnailImage);

    /** 생성한 펀딩 프로젝트 3단계 정보 확인 **/
    Boolean checkPhase3Info(FundingProjectCreatePhase3RequestDto fundingProjectCreatePhase3RequestDto, List<MultipartFile> videoAndImages);

    /** 생성한 펀딩 프로젝트 4단계 정보 확인 **/
    Boolean checkPhase4Info(FundingProjectCreatePhase4RequestDto fundingProjectCreatePhase4RequestDto);

    /** 펀딩 프로젝트의 타이틀 명은 중복될 수 없음을 확인 **/
    Boolean checkDuplicatedTitle(String projectTitle);

    /** 마지막 단계에서 시간 초과로 인해 기존의 정보들이 초기화 되어있을 경우 에러 메세지 응답 처리 **/
    Boolean checkAllPhase(
            Optional<FundingProjectCreatePhase1ResponseDto> phase1,
            Optional<FundingProjectCreatePhase2ResponseDto> phase2,
            Optional<FundingProjectCreatePhase3ResponseDto> phase3,
            Optional<FundingProjectCreatePhase4ResponseDto> phase4);


    // [상황에 따른 예외 응답 처리]

    /** 배송 여부 확인 후 특정배송 시 최소 3000원 배송비 포함, 특정배송이 아닌 기본배송일 경우 배송비 0원 반환 **/
    int deliveryChecking(String deliveryCheck);

    /** OpenReservation이 O이라면 startDate 속성의 값을 기입받은 openReservationStartDate로 넣는다.
     X라면 프로젝트가 생성되어 바로 그 즉시 시점의 시간대를 startDate 속성에 넣는다. **/
    String startDateSetting(String openReservation, String openReservationStartdate);

    /** 프로젝트를 생성한 메이커가 맞는지 확인 **/
    Boolean checkProjectMaker(Member authMember, Long projectId);

    /** 프로젝트를 수정할 때 수정값이 하나도 변경된 값이 없거나 null 값이면 수정할 수 없다는 읃답 처리 **/
    Boolean checkUpdateInfo(FundingProjectUpdateRequestDto fundingProjectUpdateRequestDto, MultipartFile thumbnailImage, List<MultipartFile> videoAndImages);

    /** 펀딩 프로젝트 생성 마지막 최종 단계에서 반드시 기입되어야할 정보가 하나라도
     * 명확하지 않다면 에러 응답 처리 **/

}
