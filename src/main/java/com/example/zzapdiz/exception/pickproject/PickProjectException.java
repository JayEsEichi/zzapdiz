package com.example.zzapdiz.exception.pickproject;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;


@RequiredArgsConstructor
@Slf4j
@Component
public class PickProjectException implements PickProjectExceptionInterface{

    private final JPAQueryFactory jpaQueryFactory;

    // 본인이 생성한 프로젝트는 찜하기 불가
    @Override
    public Boolean pickMyProjectCheck(Member authMember, FundingProject project) {

        if(jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.member.eq(authMember).and(fundingProject.fundingProjectId.eq(project.getFundingProjectId())))
                .fetchOne() != null){
            return true;
        }
        return false;
    }
}
