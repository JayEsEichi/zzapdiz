package com.example.zzapdiz.exception.dofundproject;

import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.example.zzapdiz.dofundproject.request.DoFundPhase2RequestDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase1ResponseDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase2ResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface DoFundProjectExceptionInterface {

    /** 펀딩하기 1단계 정보 확인 **/
    Boolean checkDoFundPhase1Info(List<DoFundPhase1RequestDto> doFundPhase1RequestDtos);

    /** 펀딩하기 2단계 정보 확인 **/
    Boolean checkDoFundPhase2Info(DoFundPhase2RequestDto doFundPhase2RequestDto);

    /** 자기가 생성한 프로젝트에 자기가 펀딩할 수 없다. **/
    Boolean checkMakerFundMakerProject(Long memberId, Long projectId);

    /** 전체 펀딩 진행 데이터 확인 **/
    Boolean checkAllPhase(List<DoFundPhase1ResponseDto> doFundPhase1ResponseDtos, Optional<DoFundPhase2ResponseDto> doFundPhase2ResponseDto);

    /** 입력한 펀딩 금액이 펀딩하고자 하는 총 리워드들의 금액보다 낮으면 결제 불가 처리 **/
    Boolean checkInputQuantity(int inputQuantity, int compareTotalRewardQuantity);

    /** 이미 종료중인 프로젝트는 펀딩을 할 수 없음을 처리 **/
    Boolean checkProgressBeforeFunding(Long projectId);
}
