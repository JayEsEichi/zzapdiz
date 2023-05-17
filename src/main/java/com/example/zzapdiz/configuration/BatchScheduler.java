package com.example.zzapdiz.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;

    // 매일 오전 9시에 프로젝트 종료일에 도달하거나 초과한 프로젝트 자동 종료 수행 스케줄
    @Scheduled(cron = "0 0 9 * * *")
    public void runJob(){
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try{
            jobLauncher.run(batchConfig.progressUpdateJob(), jobParameters);
        }catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException | JobRestartException e){
            log.error(e.getMessage());
        }
    }

    // 매 1시간마다 달성율에 따른 프로젝트 랭킹 리스트업 스케줄
//    @Scheduled(cron = "0 0 * * * *")
//    public void runJob2(){
//        Map<String, JobParameter> confMap = new HashMap<>();
//        confMap.put("time", new JobParameter(System.currentTimeMillis()));
//        JobParameters jobParameters = new JobParameters(confMap);
//
//        try{
//            jobLauncher.run(batchConfig.rankingProjectsUpdateJob(), jobParameters);
//        }catch(JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException | JobRestartException e){
//            log.error(e.getMessage());
//        }
//    }
}
