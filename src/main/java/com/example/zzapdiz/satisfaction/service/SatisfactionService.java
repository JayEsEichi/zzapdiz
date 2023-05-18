package com.example.zzapdiz.satisfaction.service;

import com.example.zzapdiz.exception.fundingproject.FundingProejctExceptionInterface;
import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.exception.satisfaction.SatisfactionExceptionInterface;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.satisfaction.domain.Satisfaction;
import com.example.zzapdiz.satisfaction.repository.SatisfactionRepository;
import com.example.zzapdiz.satisfaction.request.SatisfactionRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class SatisfactionService {

    private final MemberExceptionInterface memberExceptionInterface;
    private final DynamicQueryDsl dynamicQueryDsl;
    private final JwtTokenProvider jwtTokenProvider;
    private final SatisfactionRepository satisfactionRepository;
    private final SatisfactionExceptionInterface satisfactionExceptionInterface;
    private final FundingProejctExceptionInterface fundingProejctExceptionInterface;

    // 만족도 및 별점 주기
    public ResponseEntity<ResponseBody> giveSatisfaction(HttpServletRequest request, SatisfactionRequestDto satisfactionRequestDto){

        // 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 만족도를 줄 유저
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();
        // 만족도를 줄 프로젝트
        FundingProject fundingProject = dynamicQueryDsl.getFundingProject(satisfactionRequestDto.getProjectId());
        // 프로젝트에 줄 별점
        String starRateIcon = "";

        // 자기가 만든 프로젝트일 경우 만족도 및 별점을 줄 수 없음
        if(!fundingProejctExceptionInterface.checkProjectMaker(authMember, fundingProject.getFundingProjectId())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.CANT_GIVE_SATISFACTION_TO_MINE, null), HttpStatus.BAD_REQUEST);
        }

        // 별점 0점인지 확인
        if(satisfactionExceptionInterface.checkStarRate(satisfactionRequestDto.getSatisfactionStarRate())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.CANT_GIVE_SATISFACTION, null), HttpStatus.BAD_REQUEST);
        }

        // 요청된 별점 점수에 따라 별 아이콘 생성
        for(int i = 1 ; i <= satisfactionRequestDto.getSatisfactionStarRate() ; i++){
            starRateIcon += "⭐";
        }

        // 만족도 정보 엔티티에 반영
        Satisfaction satisfaction = Satisfaction.builder()
                .satisfactionContent(satisfactionRequestDto.getSatisfactionContent())
                .satisfactionStarRate(starRateIcon)
                .member(authMember)
                .fundingProject(fundingProject)
                .build();

        // 엔티티에 만족도 및 별점 정보 저장
        satisfactionRepository.save(satisfaction);

        LinkedHashMap<String, Object> resultSet = new LinkedHashMap<>();
        resultSet.put("resultMessage", "만족도를 남겨주셔서 감사합니다. ^^");
        resultSet.put("resultData", satisfaction);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }
}
