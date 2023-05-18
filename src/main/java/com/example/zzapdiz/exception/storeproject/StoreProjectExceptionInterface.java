package com.example.zzapdiz.exception.storeproject;

import org.springframework.stereotype.Component;

@Component
public interface StoreProjectExceptionInterface {

    /** 펀딩 프로젝트 당시에 펀딩한 서포터가 100명 이상 or 펀딩 달성 금액이 1000만원 이상일 경우 1차 스토어 생성 허용 **/
    Boolean checkStoreChangeCondition1(Long projectId);

    /** 펀딩 프로젝트 만족 별점이 3.5점 이상이 아닐 경우 조건에 부합하지 않아 스토어 프로젝트를 생성할 수 없음 **/
    // 만족도 기능 추가 후 업데이트 필요


    /** 펀딩 프로젝트 만족도가 5개 이상 기입되지 않았을 경우 조건에 부합하지 않아 스토어 프로젝트를 생성할 수 없음. **/
    // 만족도 기능 추가 후 업데이트 필요


}
