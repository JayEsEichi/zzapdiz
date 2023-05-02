package com.example.zzapdiz.exception.dofundproject;

import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.example.zzapdiz.dofundproject.request.DoFundPhase2RequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DoFundProjectExceptionInterface {

    /** 펀딩하기 1단계 정보 확인 **/
    Boolean checkDoFundPhase1Info(List<DoFundPhase1RequestDto> doFundPhase1RequestDtos);

    /** 펀딩하기 2단계 정보 확인 **/
    Boolean checkDoFundPhase2Info(DoFundPhase2RequestDto doFundPhase2RequestDto);

    /** 자기가 생성한 프로젝트에 자기가 펀딩할 수 없다. **/
    Boolean checkMakerFundMakerProject(Long memberId, Long projectId);

}
