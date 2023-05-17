package com.example.zzapdiz.dofundproject.service;

import com.example.zzapdiz.dofundproject.domain.DoFund;
import com.example.zzapdiz.dofundproject.repository.DoFundPhase1Repository;
import com.example.zzapdiz.dofundproject.repository.DoFundPhase2Repository;
import com.example.zzapdiz.dofundproject.repository.DoFundRepository;
import com.example.zzapdiz.dofundproject.request.DoFundPhase1RequestDto;
import com.example.zzapdiz.dofundproject.request.DoFundPhase2RequestDto;
import com.example.zzapdiz.dofundproject.request.InputQuantityDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase1ResponseDto;
import com.example.zzapdiz.dofundproject.response.DoFundPhase2ResponseDto;
import com.example.zzapdiz.dofundproject.response.FundingRewardResponseDto;
import com.example.zzapdiz.exception.dofundproject.DoFundProjectException;
import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.reward.response.RewardCreateResponseDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.example.zzapdiz.reward.domain.QReward.reward;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoFundService {

    private final DoFundPhase1Repository doFundPhase1Repository;
    private final DoFundPhase2Repository doFundPhase2Repository;
    private final DoFundRepository doFundRepository;
    private final MemberExceptionInterface memberExceptionInterface;
    private final DoFundProjectException doFundProjectException;
    private final JwtTokenProvider jwtTokenProvider;
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    private final DynamicQueryDsl dynamicQueryDsl;

    private final HashMap<Long, List<FundingRewardResponseDto>> fundingRewards = new HashMap<>();


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
        // 펀딩한 리워드들에 대한 정보를 static 변수에 1차적으로 임시 관리 저장
        List<FundingRewardResponseDto> doFundPhase1ResponseDtos = new ArrayList<>();

        DoFundPhase1ResponseDto phase1FundInfo = DoFundPhase1ResponseDto.builder()
                .memberId(memberId)
                .fundingProjectId(doFundPhase1RequestDtos.get(0).getFundingProjectId())
                .build();

        // 펀딩 정보들 저장
        for (DoFundPhase1RequestDto phase1RequestDto : doFundPhase1RequestDtos) {
            FundingRewardResponseDto phase1 = FundingRewardResponseDto.builder()
                    .memberId(memberId)
                    .fundingProjectId(phase1RequestDto.getFundingProjectId())
                    .rewardId(phase1RequestDto.getRewardId())
                    .build();

            doFundPhase1ResponseDtos.add(phase1);

            log.info("펀딩하고 있는 유저 - {}, 캐싱된 펀딩한 리워드 1단계 정보 - {}", phase1.getMemberId(), phase1.getRewardId());
        }

        fundingRewards.put(memberId, doFundPhase1ResponseDtos);
        doFundPhase1Repository.save(phase1FundInfo);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("resultMessage", "펀딩하기 1단계 성공");
        resultSet.put("resultData", doFundPhase1ResponseDtos);

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


    // 펀딩하기 3단계
    @Transactional
    public synchronized ResponseEntity<ResponseBody> doFundPhase3(HttpServletRequest request, InputQuantityDto inputQuantityDto) {

        // 펀딩하는 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 펀딩하고자 하는 유저의 ID
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();

        // 펀딩 진행중인 데이터 정보들 불러오기
        List<DoFundPhase1ResponseDto> phase1Info = (List<DoFundPhase1ResponseDto>) doFundPhase1Repository.findAllById(Collections.singleton(authMember.getMemberId()));
        Optional<DoFundPhase2ResponseDto> phase2Info = doFundPhase2Repository.findById(authMember.getMemberId());

        // 이미 종료된 프로젝트는 펀딩할 수 없음을 확인
        if(doFundProjectException.checkProgressBeforeFunding(phase1Info.get(0).getFundingProjectId())){
            return new ResponseEntity<>(new ResponseBody(StatusCode.CANT_FUND_FOR_END_PROJECT, null), HttpStatus.BAD_REQUEST);
        }

        // 현재 진행 중인 전체 펀딩하기 데이터 확인
        if (doFundProjectException.checkAllPhase(phase1Info, phase2Info)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.NOT_EXIST_FUND_DATA, null), HttpStatus.BAD_REQUEST);
        }

        int fundRewardTotalQuantity = 0;
        List<Reward> fundRewardList = new ArrayList<>();

        for(FundingRewardResponseDto eachPhase1 : fundingRewards.get(authMember.getMemberId())){
            Reward fundReward = jpaQueryFactory
                    .selectFrom(reward)
                    .where(reward.rewardId.eq(eachPhase1.getRewardId()))
                    .fetchOne();

            fundRewardList.add(fundReward);

            fundRewardTotalQuantity += fundReward.getRewardQuantity();
        }

        // 펀딩하고자 하는 총 리워드 금액보다 입력한 결제 금액이 작으면 결제 불가 처리
        if(doFundProjectException.checkInputQuantity(inputQuantityDto.getQuantity(), fundRewardTotalQuantity)){
            return new ResponseEntity<>(new ResponseBody(StatusCode.CANT_FUNDING, null), HttpStatus.BAD_REQUEST);
        }

        DoFund doFund = DoFund.builder()
                .doFundQuantity(fundRewardTotalQuantity)
                .phoneNumber(phase2Info.get().getPhoneNumber())
                .address(phase2Info.get().getAddress())
                .point(phase2Info.get().getPoint())
                .donation(phase2Info.get().getDonation() + (inputQuantityDto.getQuantity() - fundRewardTotalQuantity))
                .member(authMember)
                .fundingProjectId(phase1Info.get(0).getFundingProjectId())
                .rewards(fundRewardList)
                .build();

        doFundRepository.save(doFund);

        // 펀딩에 성공하면 리워드 수량 1개씩 제거 (락을 걸어 동시성 이슈 제거)
        for(Reward eachReward : fundRewardList){
            int updateAmount = eachReward.getRewardAmount() - 1;

            jpaQueryFactory
                    .update(reward)
                    .set(reward.rewardAmount, updateAmount)
                    .where(reward.rewardId.eq(eachReward.getRewardId()))
                    .execute();

            entityManager.flush();
            entityManager.clear();
        }

        // 펀딩 완료 후 FundingProject 엔티티 CollectQuantity 속성에 펀딩 금액 추가 반영
        dynamicQueryDsl.updateCollectQuantity(phase2Info.get().getDonation() + (inputQuantityDto.getQuantity() - fundRewardTotalQuantity), phase1Info.get(0).getFundingProjectId());

        // 남아있는 임시저장된 Redis 객체들, 펀딩 정보들 삭제
        doFundPhase1Repository.deleteAllById(Collections.singleton(authMember.getMemberId()));
        doFundPhase2Repository.deleteById(authMember.getMemberId());

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("resultMessage", "마지막 최종 펀딩 성공");
        resultSet.put("resultData", doFund);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }
}
