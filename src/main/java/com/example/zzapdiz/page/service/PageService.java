package com.example.zzapdiz.page.service;

import com.example.zzapdiz.page.request.ExhibitionRequestDto;
import com.example.zzapdiz.page.response.ExhibitionProjectsResponseDto;
import com.example.zzapdiz.page.response.LankingProjectsResponseDto;
import com.example.zzapdiz.page.response.RecentOpenProjectsResponseDto;
import com.example.zzapdiz.page.response.SuitableProjectsResponseDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageService {

    private final DynamicQueryDsl dynamicQueryDsl;
//    public static LinkedHashMap<String, Object> resultSet = new LinkedHashMap<>();

    // 짭디즈 메인 페이지 접속
    public ResponseEntity<ResponseBody> readMainPage(ExhibitionRequestDto exhibitionRequestDto) {

//        Thread threadHelper = new Thread(new PageThreadHelper(dynamicQueryDsl));
//        threadHelper.start();

        // 1. 취향저격 프로젝트 4개 조회
        List<SuitableProjectsResponseDto> suitableProjects =  dynamicQueryDsl.getSuitableFundingProjects();

        // 2. 메인 페이지에 접속하게 될 때 출력되어야 할 프로젝트 정보들 로직 생성 필요
        List<LankingProjectsResponseDto> lankingProjects = dynamicQueryDsl.getLankingProjects();

        // 3. 스토어 추천 제품 recommencStoreProjects (스토어 프로젝트 랜덤 6개)


        // 4. 최근 오픈한 펀딩 프로젝트 recentProjects (가장 최근에 오픈한 펀딩 프로젝트 6개 리스트 업)
        List<RecentOpenProjectsResponseDto> recentProjects = dynamicQueryDsl.getRecentProjects();

        // 5. 기획전 exhibition (각각 다른 카테고리 3개 지정하여 생성 후 그 카테고리에 맞는 프로젝트 3개씩 리스트 업)
        HashMap<String, List<ExhibitionProjectsResponseDto>> exhibitionProjects = dynamicQueryDsl.getExhibitionProjects(exhibitionRequestDto);

        // 메인 페이지에 출력되어야할 프로젝트 정보들
        LinkedHashMap<String, Object> resultSet = new LinkedHashMap<>();
        resultSet.put("resultMessage", "메인 페이지 정보 출력 성공");
        resultSet.put("suitableProjects", suitableProjects);
        resultSet.put("lankingProjects", lankingProjects);
//        resultSet.put("", ); - 스토어 추천 제품 리스트
        resultSet.put("recentProjects", recentProjects);
        resultSet.put("exhibitionProjects", exhibitionProjects);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }
}
