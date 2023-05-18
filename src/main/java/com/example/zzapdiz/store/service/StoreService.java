package com.example.zzapdiz.store.service;

import com.example.zzapdiz.exception.fundingproject.FundingProejctExceptionInterface;
import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.exception.storeproject.StoreProjectExceptionInterface;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.example.zzapdiz.store.domain.StoreProject;
import com.example.zzapdiz.store.repository.StoreRepository;
import com.example.zzapdiz.store.request.StoreCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberExceptionInterface memberExceptionInterface;
    private final FundingProejctExceptionInterface fundingProejctExceptionInterface;
    private final DynamicQueryDsl dynamicQueryDsl;

    private final StoreProjectExceptionInterface storeProjectExceptionInterface;

    // 스토어 프로젝트 생성
    public ResponseEntity<ResponseBody> createStoreProject(HttpServletRequest request, StoreCreateRequestDto storeCreateRequestDto){

        // 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 생성하는 유저의 id
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();

        // 스토어 프로젝트로 변경하고자 하는 펀딩 프로젝트가 요청한 유저가 생성한 프로젝트가 맞는지 확인
        if(!fundingProejctExceptionInterface.checkProjectMaker(authMember, storeCreateRequestDto.getProjectId())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.NOT_CORRECT_MAKER, null), HttpStatus.BAD_REQUEST);
        }

        FundingProject fundingProject = dynamicQueryDsl.getFundingProject(storeCreateRequestDto.getProjectId());

        // 펀딩된 금액이 1000만원 이상이거나 펀딩한 인원 수가 100명 이상인 조건을 달성하지 않았을 경우 확인
        if(!storeProjectExceptionInterface.checkStoreChangeCondition1(fundingProject.getFundingProjectId())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.CANT_CREATE_STORE_PROJECT, null), HttpStatus.BAD_REQUEST);
        }

        // 펀딩 프로젝트 만족 별점이 3.5점 이상이 아닐 경우 조건에 부합하지 않아 스토어 프로젝트를 생성할 수 없음.

        // 펀딩 프로젝트 만족도가 5개 이상 기입되지 않았을 경우 조건에 부합하지 않아 스토어 프로젝트를 생성할 수 없음.

        StoreProject storeProject = StoreProject.builder()
                .projectTitle(fundingProject.getProjectTitle())
                .projectCategory(fundingProject.getProjectCategory())
                .projectType(fundingProject.getProjectType())
                .adultCheck(fundingProject.getAdultCheck())
                .searchTag(fundingProject.getSearchTag())
                .storyText(fundingProject.getStoryText())
                .projectDescript(fundingProject.getProjectDescript())
                .deliveryCheck(fundingProject.getDeliveryCheck())
                .deliveryPrice(fundingProject.getDeliveryPrice())
                .fundingProjectId(fundingProject.getFundingProjectId())
                .member(authMember)
                .build();

        storeRepository.save(storeProject);

        LinkedHashMap<String, Object> resultSet = new LinkedHashMap<>();
        resultSet.put("resultMessage", "스토어 프로젝트로 전환 성공하셨습니다.");
        resultSet.put("resultData", storeProject);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }
}
