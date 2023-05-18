package com.example.zzapdiz.exception.storeproject;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.zzapdiz.dofundproject.domain.QDoFund.doFund;
import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;

@RequiredArgsConstructor
@Component
public class StoreProjectException implements StoreProjectExceptionInterface {

    private final JPAQueryFactory jpaQueryFactory;

    // 펀딩 프로젝트 당시에 펀딩한 서포터가 100명 이상 or 펀딩 달성 금액이 1000만원 이상일 경우 1차 스토어 생성 허용
    @Override
    public Boolean checkStoreChangeCondition1(Long projectId) {
        Integer fundingCount = jpaQueryFactory
                .select(doFund.count().intValue())
                .from(doFund)
                .where(doFund.fundingProjectId.eq(projectId))
                .fetchOne();

        Integer collectQuantity = jpaQueryFactory
                .select(fundingProject.collectQuantity)
                .from(fundingProject)
                .where(fundingProject.fundingProjectId.eq(projectId))
                .fetchOne();

        if (fundingCount >= 100 || collectQuantity >= 10000000) {
            return true;
        }

        return false;
    }
}
