package com.example.zzapdiz.exception.supportproject;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;

@RequiredArgsConstructor
@Component
public class DoSupportException implements DoSupportExceptionInterface{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Boolean supportMyProjectCheck(Member authMember, FundingProject project) {
        if(jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.member.eq(authMember).and(fundingProject.fundingProjectId.eq(project.getFundingProjectId())))
                .fetchOne() != null){
            return true;
        }

        return false;
    }
}
