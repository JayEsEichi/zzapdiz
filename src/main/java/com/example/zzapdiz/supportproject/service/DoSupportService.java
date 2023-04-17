package com.example.zzapdiz.supportproject.service;

import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.exception.supportproject.DoSupportExceptionInterface;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.example.zzapdiz.supportproject.domain.DoSupport;
import com.example.zzapdiz.supportproject.repository.DoSupportRepository;
import com.example.zzapdiz.supportproject.response.DoSupportResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class DoSupportService {

    private final MemberExceptionInterface memberExceptionInterface;
    private final DoSupportExceptionInterface doSupportExceptionInterface;
    private final JwtTokenProvider jwtTokenProvider;
    private final DynamicQueryDsl dynamicQueryDsl;
    private final DoSupportRepository doSupportRepository;

    // 프로젝트 지지하기
    public ResponseEntity<ResponseBody> supportProject(HttpServletRequest request, Long projectId){

        // 유저 유효성 검증
        if(memberExceptionInterface.checkHeaderToken(request)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 지지하고자 하는 유저의 정보 조회 + 지지하고자 하는 프로젝트 정보 조회
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();
        FundingProject supportProject = dynamicQueryDsl.getFundingProject(projectId);

        // 본인이 생성한 프로젝트는 지지할 수 없음을 확인
        if(doSupportExceptionInterface.supportMyProjectCheck(authMember, supportProject)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.CANT_SUPPORT_MINE, null), HttpStatus.BAD_REQUEST);
        }

        // 지지를 이미 한 번 했으면 취소로 처리
        if (dynamicQueryDsl.supportCancel(authMember, supportProject)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "지지 취소!"), HttpStatus.OK);
        }

        // 지지하기 정보 실반영
        DoSupport doSupport = DoSupport.builder()
                .member(authMember)
                .fundingProject(supportProject)
                .build();

        doSupportRepository.save(doSupport);

        // 지지수 업데이트
        dynamicQueryDsl.supportCountUpdate(supportProject);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("supportMessage", "지지!");
        resultSet.put("supportInfo", doSupportResponseDto(doSupport));

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }

    private DoSupportResponseDto doSupportResponseDto(DoSupport doSupport){
        return DoSupportResponseDto.builder()
                .doSupportId(doSupport.getDoSupportId())
                .memberId(doSupport.getMember().getMemberId())
                .fundingProjectId(doSupport.getFundingProject().getFundingProjectId())
                .build();
    }
}
