package com.example.zzapdiz.exception.dofundproject;

import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
