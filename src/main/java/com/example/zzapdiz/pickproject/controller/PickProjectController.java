package com.example.zzapdiz.pickproject.controller;

import com.example.zzapdiz.pickproject.service.PickProjectService;
import com.example.zzapdiz.share.ResponseBody;
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
public class PickProjectController {

    private final PickProjectService pickProjectService;

    // 프로젝트 찜하기
    @PostMapping("/project/pick/{projectId}")
    public ResponseEntity<ResponseBody> pickProject(HttpServletRequest request, @PathVariable Long projectId){
        log.info("프로젝트 찜하기 api : 요청자 - {}, 프로젝트 id - {}",request, projectId);

        return pickProjectService.pickProject(request, projectId);
    }
}
