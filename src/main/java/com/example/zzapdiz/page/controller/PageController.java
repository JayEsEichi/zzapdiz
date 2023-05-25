package com.example.zzapdiz.page.controller;


import com.example.zzapdiz.page.request.ExhibitionRequestDto;
import com.example.zzapdiz.page.service.PageService;
import com.example.zzapdiz.share.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/zzapdiz/page")
@RestController
public class PageController {

    private final PageService pageService;

    // 짭디즈 메인 페이지 접속
    @GetMapping("/main")
    public ResponseEntity<ResponseBody> readMainPage(@RequestBody ExhibitionRequestDto exhibitionRequestDto){
        log.info("짭디즈 메인 페이지 접속");

        return pageService.readMainPage(exhibitionRequestDto);
    }
}
