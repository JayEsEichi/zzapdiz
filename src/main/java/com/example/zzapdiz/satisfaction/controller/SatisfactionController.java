package com.example.zzapdiz.satisfaction.controller;

import com.example.zzapdiz.satisfaction.request.SatisfactionRequestDto;
import com.example.zzapdiz.satisfaction.service.SatisfactionService;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/zzapdiz")
@RestController
public class SatisfactionController {

    private final SatisfactionService satisfactionService;

    // 만족도 및 별점 주기
    @PostMapping("/satisfaction/give")
    public ResponseEntity<ResponseBody> giveSatisfaction(
            HttpServletRequest request, @RequestBody SatisfactionRequestDto satisfactionRequestDto){
        log.info("만족도 주기 : 요청 회원 - {}, 해당 프로젝트 id - {}, 별점 - {}", request, satisfactionRequestDto.getProjectId(), satisfactionRequestDto.getSatisfactionStarRate());

        return satisfactionService.giveSatisfaction(request, satisfactionRequestDto);
    }
}
