package com.example.zzapdiz.other;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;

import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;

@SpringBootTest
public class OtherTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    // 날짜 빼기 테스트
    @DisplayName("날짜 빼기 테스트")
    @Test
    void dateCalculateTest() {
        FundingProject testProject = jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.fundingProjectId.eq(1L))
                .fetchOne();


        int projectYear = testProject.getEndDate().getYear();
        int projectMonth = testProject.getEndDate().getMonthValue();
        int projectDay = testProject.getEndDate().getDayOfYear();

        int nowYear = LocalDateTime.now().getYear();
        int nowMonth = LocalDateTime.now().getDayOfMonth();
        int nowDay = LocalDateTime.now().getDayOfYear();

        System.out.println("프로젝트 end_date에 기입된 날짜 정보");
        System.out.println("년도 - " + projectYear + " 월 - " + projectMonth + " 일 - " + projectDay);

        System.out.println("현재 날짜 기준 날짜 정보");
        System.out.println("넌도 - " + nowYear + " 월 - " + nowMonth + " 일 - " + nowDay);

        System.out.println("테스트 결과 보여주세요!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(LocalDateTime.now().get);
    }
}
