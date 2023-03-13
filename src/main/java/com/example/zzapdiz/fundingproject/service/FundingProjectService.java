package com.example.zzapdiz.fundingproject.service;

import com.example.zzapdiz.exception.member.MemberException;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundingProjectService {

    private final MemberException memberException;

    // 펀딩 프로젝트 생성 1단계
    public ResponseEntity<ResponseBody> fundingCreatePhase1(
            HttpServletRequest request,
            FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDt0){

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        memberException.checkHeaderToken(request);

        // Request에 Phase1 생성 단계 속성 생성
        request.setAttribute("phase1Info", fundingProjectCreatePhase1RequestDt0);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "펀딩 프로젝트 생성 1단계 완료!"), HttpStatus.OK);
    }

    // Gson().toJson("Json 객체 혹은 Dto 객체") -> Json 형식의 객체를 String 타입으로 변환
    // Gson().fromJson("String 타입으로 변환된 객체", 변환될 Json 형식객체 혹은 DTO 객체.class) -> String 타입으로 변환된 Json 객체를 다시 Json 형식으로 변환
}


