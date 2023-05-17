package com.example.zzapdiz.configuration;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.page.service.PageService;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;

@Slf4j
@RequiredArgsConstructor
@Configuration
//@EnableBatchProcessing
public class BatchConfig {

//    private static String nowDate = LocalDateTime.now().toString();

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JPAQueryFactory jpaQueryFactory;
    private final DynamicQueryDsl dynamicQueryDsl;
    private final PageService pageService;


    // 스케줄러와 배치를 통한 작업 (프로젝트 자동 종료)
    @Bean
    public Job progressUpdateJob() {

        return jobBuilderFactory.get("job")
                .start(step())
                .build();
    }

    // 스케줄러와 배치를 통한 작업 (랭킹 프로젝트 업데이트 리스트업)
//    @Bean
//    public Job rankingProjectsUpdateJob() {
//
//        return jobBuilderFactory.get("job")
//                .start(step2())
//                .build();
//    }


    // job을 수행하기 위한 step 과정
    @Bean
    public Step step() {
        // 프로젝트 종료일과 비교할 현재 날짜
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 날짜와 시간 포맷 형식
        String nowDate = LocalDateTime.now().toString().replace('T', ' '); // 날짜 시간 문자열 중간에 있는 T 문자 삭제
        String[] dateSplit = nowDate.split("\\."); // 포맷 형식에 맞게끔 . 기호를 기준으로 필요한 문자열만 추출
        LocalDateTime nowDateTime = LocalDateTime.parse(dateSplit[0], formatter); // 추출한 날짜 데이터에 포맷 형식 적용

        // 스케줄러를 통해 수행될 step 작업
        return stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("step!");

                    // 현재 진행 중인 상태이고 현재 날짜가 프로젝트 종료일을 넘긴 프로젝트들 조회
                    List<FundingProject> fundingProjects = jpaQueryFactory
                            .selectFrom(fundingProject)
                            .where(fundingProject.progress.eq("진행중").and(fundingProject.endDate.loe(nowDateTime)))
                            .fetch();

                    // 자동 종료시킬 프로젝트가 존재할 경우 작업 수행
                    if (!fundingProjects.isEmpty()) {
                        // 각 프로젝트들의 진행상황을 종료로 변환
                        for (FundingProject eachProject : fundingProjects) {
                            dynamicQueryDsl.updateProjectProgress(eachProject.getFundingProjectId());
                        }
                    }

                    return RepeatStatus.FINISHED;
                }).build();
    }

//    @Bean
//    public Step step2(){
//        return stepBuilderFactory.get("step")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("step!");
//
//                    /**
//                     * 펀딩 달성율 구하는 로직을 만들고 해당 달성율이 가장 높은 프로젝트를 스케줄러를 활용하여
//                     * 매 시간 (1시간 간격) 마다 업데이트하는 step 만들어야 함.
//                     *
//                     */
//
//                    // 굳이 스케줄러랑 배치로 해야할까?
//                    // pageService.readMainPage();
//
//                    return RepeatStatus.FINISHED;
//                }).build();
//    }

}
