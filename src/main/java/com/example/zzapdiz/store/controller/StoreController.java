package com.example.zzapdiz.store.controller;

import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.store.request.StoreCreateRequestDto;
import com.example.zzapdiz.store.service.StoreService;
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
public class StoreController {

    private final StoreService storeService;

    // 스토어 프로젝트 생성 (전환)
    @PostMapping("/store/create")
    public ResponseEntity<ResponseBody> createStoreProject(HttpServletRequest request, @RequestBody StoreCreateRequestDto storeCreateRequestDto){
        log.info("스토어 생성 api : 요청 유저 - {}, 프로젝트 id - {}", request, storeCreateRequestDto.getProjectId());

        return storeService.createStoreProject(request, storeCreateRequestDto);
    }
}
