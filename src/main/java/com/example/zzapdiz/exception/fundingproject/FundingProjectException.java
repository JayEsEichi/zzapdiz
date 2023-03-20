package com.example.zzapdiz.exception.fundingproject;

import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase3RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase4RequestDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase1ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase2ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase3ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase4ResponseDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;

@RequiredArgsConstructor
@Component
public class FundingProjectException implements FundingProejctExceptionInterface {

    private final JPAQueryFactory jpaQueryFactory;

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

    // 펀딩 프로젝트 생성 4단계 기입 정보들 확인
    @Override
    public ResponseEntity<ResponseBody> checkPhase4Info(FundingProjectCreatePhase4RequestDto fundingProjectCreatePhase4RequestDto) {
        if (fundingProjectCreatePhase4RequestDto.getDeliveryCheck() == null ||
        fundingProjectCreatePhase4RequestDto.getDeliveryStartDate() == null) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    // 펀딩 프로젝트 중복 타이틀 확인
    @Override
    public ResponseEntity<ResponseBody> checkDuplicatedTitle(String projectTitle) {
        if(jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.projectTitle.eq(projectTitle))
                .fetchOne() != null){
            return new ResponseEntity<>(new ResponseBody(StatusCode.DUPLICATED_PROJECT_TITLE, null), HttpStatus.OK);
        }

        return null;
    }

    // 시간 초과로 인한 프로젝트 단계 정보들 초기화 발생 시 에러 응답 처리
    @Override
    public ResponseEntity<ResponseBody> checkAllPhase(
            Optional<FundingProjectCreatePhase1ResponseDto> phase1, Optional<FundingProjectCreatePhase2ResponseDto> phase2,
            Optional<FundingProjectCreatePhase3ResponseDto> phase3, Optional<FundingProjectCreatePhase4ResponseDto> phase4) {
        if(phase1.isEmpty() || phase2.isEmpty() || phase3.isEmpty() || phase4.isEmpty()){
            return new ResponseEntity<>(new ResponseBody(StatusCode.TIME_LIMIT_CHECK, null), HttpStatus.BAD_REQUEST);
        }

        return null;
    }


    // 특정 값이 반환되야하는 예외 처리일 경우

    // 배송 여부 확인 후 특정배송 시 최소 3000원 배송비 포함, 특정배송이 아닌 기본배송일 경우 배송비 0원 반환
    public int deliveryChecking(String deliveryCheck){
        int deliveryPrice = 0;

        if(deliveryCheck.equals("O")){
            deliveryPrice = 3000;

            return deliveryPrice;
        }else{
            return deliveryPrice;
        }
    }


    // OpenReservation이 O이라면 startDate 속성의 값을 기입받은 openReservationStartDate로 넣는다.
    // X라면 프로젝트가 생성되 바로 그 즉시 시점의 시간대를 startDate 속성에 넣는다.
    public String startDateSetting(String openReservation, String openReservationStartdate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (openReservation.equals("X")){
            return LocalDateTime.now().format(formatter);
        }else{;
            return openReservationStartdate;
        }
    }


}
