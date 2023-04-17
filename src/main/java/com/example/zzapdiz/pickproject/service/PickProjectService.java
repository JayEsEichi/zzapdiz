package com.example.zzapdiz.pickproject.service;

import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.exception.pickproject.PickProjectException;
import com.example.zzapdiz.exception.pickproject.PickProjectExceptionInterface;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.pickproject.domain.PickProject;
import com.example.zzapdiz.pickproject.repository.PickProjectRepository;
import com.example.zzapdiz.pickproject.response.PickProjectResponseDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class PickProjectService {

    private final MemberExceptionInterface memberExceptionInterface;
    private final PickProjectExceptionInterface pickProjectExceptionInterface;
    private final DynamicQueryDsl dynamicQueryDsl;
    private final JwtTokenProvider jwtTokenProvider;
    private final PickProjectRepository pickProjectRepository;

    // 프로젝트 찜하기
    public ResponseEntity<ResponseBody> pickProject(HttpServletRequest request, Long projectId) {

        // 유저 유효성 검증
        if(memberExceptionInterface.checkHeaderToken(request)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 찜하려는 유저의 정보와 찜하려는 프로젝트 조회
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();
        FundingProject pickFundingProject = dynamicQueryDsl.getFundingProject(projectId);

        // 본인이 생성한 프로젝트를 찜한 것인지 확인
        if(pickProjectExceptionInterface.pickMyProjectCheck(authMember, pickFundingProject)){
            return new ResponseEntity<>(new ResponseBody<>(StatusCode.CANT_PICK_MINE, null), HttpStatus.BAD_REQUEST);
        }

        // 이전에 같은 유저가 같은 프로젝트를 찜한 적이 있다면 찜 취소
        if(dynamicQueryDsl.pickCancel(authMember, pickFundingProject)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "찜 취소!"), HttpStatus.OK);
        }

        // 찜하기 정보 실반영
        PickProject pickProject = PickProject.builder()
                .member(authMember)
                .fundingProject(pickFundingProject)
                .build();

        pickProjectRepository.save(pickProject);

        // 찜하기 수 업데이트
        dynamicQueryDsl.pickCountUpdate(pickFundingProject);

        // 출력 결과 확인 HashMap
        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("pickMessage", "찜!");
        resultSet.put("pickInfo", pickProjectResponseDto(pickProject));

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }

    // 결과 확인을 위한 Dto 객체 생성 함수
    private PickProjectResponseDto pickProjectResponseDto(PickProject pickProject){

        return PickProjectResponseDto.builder()
                .pickProjectId(pickProject.getPickProjectId())
                .memberId(pickProject.getMember().getMemberId())
                .fundingProjectId(pickProject.getFundingProject().getFundingProjectId())
                .build();
    }
}
