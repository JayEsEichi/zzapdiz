package com.example.zzapdiz;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class ZzapdizApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzapdizApplication.class, args);
        System.out.println("어플리케이션 실행 ~~~~");
    }

}
