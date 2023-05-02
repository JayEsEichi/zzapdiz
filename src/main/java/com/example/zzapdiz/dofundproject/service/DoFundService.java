package com.example.zzapdiz.dofundproject.service;

import com.example.zzapdiz.dofundproject.repository.DoFundPhase1Repository;
import com.example.zzapdiz.dofundproject.repository.DoFundPhase2Repository;
import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.example.zzapdiz.dofundproject.request.DoFundPhase2RequestDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase1ResponseDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase2ResponseDto;
import com.example.zzapdiz.exception.dofundproject.DoFundProjectException;
import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoFundService {

    private final DoFundPhase1Repository doFundPhase1Repository;
    private final DoFundPhase2Repository doFundPhase2Repository;
    private final MemberExceptionInterface memberExceptionInterface;
    private final DoFundProjectException doFundProjectException;
    private final JwtTokenProvider jwtTokenProvider;

    // 펀딩하기 1단계
    public ResponseEntity<ResponseBody> doFundPhase1(HttpServletRequest request, List<DoFundPhase1RequestDto> doFundPhase1RequestDtos) {

        // 펀딩하는 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 펀딩하기 정보 확인
        if (!doFundProjectException.checkDoFundPhase1Info(doFundPhase1RequestDtos)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.INCORRECTABLE_DOFUND_INFO, null), HttpStatus.BAD_REQUEST);
        }

        // 펀딩하고자 하는 유저의 ID
        Long memberId = jwtTokenProvider.getMemberFromAuthentication().getMemberId();

        // 메이커는 자기가 만든 프로젝트를 펀딩할 수 없음을 확인
        if (!doFundProjectException.checkMakerFundMakerProject(memberId, doFundPhase1RequestDtos.get(0).getFundingProjectId())) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.CANT_FUND_MINE, null), HttpStatus.BAD_REQUEST);
        }

        // 펀딩하기 1단계 정보가 존재하는 상태에서 다시 펀딩하기 1단계를 진행할 경우 기존 데이터 삭제
        if (doFundPhase1Repository.findAllByMemberIdAndFundingProjectId(memberId, doFundPhase1RequestDtos.get(0).getFundingProjectId()).isPresent()) {
            doFundPhase1Repository.deleteAllById(Collections.singleton(memberId));
        }

        // 1단계 정보들을 반환해주기 위해 미리 담아놓기 위한 컬렉션 객체 생성
        List<DoFundPhase1ResponseDto> phase1InfoList = new ArrayList<>();

        // 펀딩 정보들 저장
        for (DoFundPhase1RequestDto phase1RequestDto : doFundPhase1RequestDtos) {
            DoFundPhase1ResponseDto phase1 = DoFundPhase1ResponseDto.builder()
                    .memberId(memberId)
                    .fundingProjectId(phase1RequestDto.getFundingProjectId())
                    .rewardId(phase1RequestDto.getRewardId())
                    .build();

            doFundPhase1Repository.save(phase1);

            log.info("펀딩하고 있는 유저 - {}, 캐싱된 펀딩한 리워드 1단계 정보 - {}", phase1.getMemberId(), phase1.getRewardId());
            phase1InfoList.add(phase1);

        }

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("resultMessage", "펀딩하기 1단계 성공");
        resultSet.put("resultData", phase1InfoList);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }

    // 펀딩하기 2단계
    public ResponseEntity<ResponseBody> doFundPhase2(HttpServletRequest request, DoFundPhase2RequestDto doFundPhase2RequestDto) {

        // 펀딩하는 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 펀딩하기 2단계 정보 확인
        if (!doFundProjectException.checkDoFundPhase2Info(doFundPhase2RequestDto)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.INCORRECTABLE_DOFUND_INFO, null), HttpStatus.BAD_REQUEST);
        }

        // 펀딩하고자 하는 유저의 ID
        Long memberId = jwtTokenProvider.getMemberFromAuthentication().getMemberId();

        // 펀딩하기 2단계 정보가 존재하는 상태에서 다시 펀딩하기 2단계를 진행할 경우 기존 데이터 삭제
        if (doFundPhase2Repository.findAllByMemberId(memberId).isPresent()) {
            doFundPhase2Repository.deleteById(memberId);
        }

        // 펀딩하기 2단계 정보 객체에 저장
        DoFundPhase2ResponseDto phase2 = DoFundPhase2ResponseDto.builder()
                .memberId(memberId)
                .point(doFundPhase2RequestDto.getPoint())
                .address(doFundPhase2RequestDto.getAddress())
                .phoneNumber(doFundPhase2RequestDto.getPhoneNumber())
                .couponId(doFundPhase2RequestDto.getCouponId())
                .donation(doFundPhase2RequestDto.getDonation())
                .build();

        // 2단계 정보들 저장
        doFundPhase2Repository.save(phase2);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("resultMessage", "펀딩하기 2단계 성공");
        resultSet.put("resultData", phase2);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }
}
