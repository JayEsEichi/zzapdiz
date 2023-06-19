package com.example.zzapdiz;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;


@EnableRedisHttpSession
@EnableRedisRepositories
@EnableBatchProcessing
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class ZzapdizApplication {

    // Docker 구축 테스트
    @RequestMapping("/")
    public ResponseEntity<String> buildResponse(){
        return new ResponseEntity<>("안녕 도커로 켜진 스프링아", HttpStatus.OK);
    }

    public static void main(String[] args) {
        SpringApplication.run(ZzapdizApplication.class, args);
        System.out.println("어플리케이션 실행 ~~~~");
    }

}
