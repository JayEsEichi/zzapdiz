//package com.example.zzapdiz.share.thread;
//
//import com.example.zzapdiz.page.response.LankingProjectsResponseDto;
//import com.example.zzapdiz.page.response.SuitableProjectsResponseDto;
//import com.example.zzapdiz.page.service.PageService;
//import com.example.zzapdiz.share.query.DynamicQueryDsl;
//import lombok.RequiredArgsConstructor;
//
//import java.util.LinkedHashMap;
//
//@RequiredArgsConstructor
//public class PageThreadHelper implements Runnable{
//
//    private final DynamicQueryDsl dynamicQueryDsl;
//
//    @Override
//    public void run() {
//        PageService.resultSet = getProjects();
//    }
//
//    private LinkedHashMap<String, Object>  getProjects(){
//        LinkedHashMap<String, Object> resultSet = new LinkedHashMap<>();
//        resultSet.put("suitableProjects", dynamicQueryDsl.getSuitableFundingProjects());
//        resultSet.put("lankingProjects", dynamicQueryDsl.getLankingProjects());
////        resultSet.put("", ); - 스토어 추천 제품 리스트
//        resultSet.put("recentProjects", dynamicQueryDsl.getRecentProjects());
//
//        return resultSet;
//    }
//
//}
