package com.example.zzapdiz.supportproject.controller;

import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.supportproject.service.DoSupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/zzapdiz")
@RestController
public class DoSupportController {

    private final DoSupportService doSupportService;

    // 프로젝트 지지하기
    @PostMapping("/project/support/{projectId}")
    public ResponseEntity<ResponseBody> supportProject(HttpServletRequest request, @PathVariable Long projectId){
        log.info("프로젝트 지지하기 api : 요청자 - {}, 프로젝트 ID - {}", request, projectId);

        return doSupportService.supportProject(request, projectId);
    }
}
