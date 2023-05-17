package com.example.zzapdiz.exception.dofundproject;

import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.example.zzapdiz.dofundproject.request.DoFundPhase2RequestDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase1ResponseDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase2ResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;

@RequiredArgsConstructor
@Component
public class DoFundProjectException implements DoFundProjectExceptionInterface {

    private final JPAQueryFactory jpaQueryFactory;

    // 펀딩하기 1단계 정보 확인
    @Override
    public Boolean checkDoFundPhase1Info(List<DoFundPhase1RequestDto> doFundPhase1RequestDtos) {

        if (doFundPhase1RequestDtos.size() == 0 ||
                doFundPhase1RequestDtos.get(0).getFundingProjectId() == 0L ||
                doFundPhase1RequestDtos.get(0).getRewardId() == 0L) {
            return false;
        }

        return true;
    }

    // 펀딩하기 2단계 정보 확인
    @Override
    public Boolean checkDoFundPhase2Info(DoFundPhase2RequestDto doFundPhase2RequestDto) {

        if (doFundPhase2RequestDto.getAddress() == null || doFundPhase2RequestDto.getAddress().equals("") ||
        doFundPhase2RequestDto.getPhoneNumber() == null || doFundPhase2RequestDto.getPhoneNumber().equals("") ||
        doFundPhase2RequestDto.getPhoneNumber().length() != 11) {
            return false;
        }

        return true;
    }


    // 펀딩 메이커가 자기 자신의 프로젝트를 펀딩하고 있는지 확인
    @Override
    public Boolean checkMakerFundMakerProject(Long memberId, Long projectId) {

        if(jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.member.memberId.eq(memberId)
                        .and(fundingProject.fundingProjectId.eq(projectId)))
                .fetchOne() != null){
            return false;
        }

        return true;
    }

    // 전체 펀딩 진행 정보 확인
    @Override
    public Boolean checkAllPhase(List<DoFundPhase1ResponseDto> doFundPhase1ResponseDtos, Optional<DoFundPhase2ResponseDto> doFundPhase2ResponseDto) {

        return doFundPhase1ResponseDtos.isEmpty() || doFundPhase2ResponseDto.isEmpty();
    }

    // 입력한 펀딩 금액이 펀딩하고자 하는 총 리워드들의 금액보다 낮으면 결제 불가 처리
    @Override
    public Boolean checkInputQuantity(int inputQuantity, int compareTotalRewardQuantity) {

        return inputQuantity < compareTotalRewardQuantity;
    }

    // 이미 종료중인 프로젝트라면 펀딩을 할 수 없도록 처리
    @Override
    public Boolean checkProgressBeforeFunding(Long projectId) {

        return jpaQueryFactory
                .select(fundingProject.progress)
                .from(fundingProject)
                .where(fundingProject.fundingProjectId.eq(projectId))
                .fetchOne().equals("종료");
    }
}
