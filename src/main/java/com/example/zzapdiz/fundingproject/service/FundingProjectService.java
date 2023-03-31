package com.example.zzapdiz.fundingproject.service;

import com.example.zzapdiz.exception.fundingproject.FundingProejctExceptionInterface;
import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.exception.reward.RewardExceptionInterface;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.fundingproject.repository.*;
import com.example.zzapdiz.fundingproject.request.*;
import com.example.zzapdiz.fundingproject.response.*;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.reward.repository.RewardRepository;
import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.reward.response.RewardCreateResponseDto;
import com.example.zzapdiz.rewardoption.domain.RewardOption;
import com.example.zzapdiz.rewardoption.repository.RewardOptionRepository;
import com.example.zzapdiz.share.media.Media;
import com.example.zzapdiz.share.media.MediaUploadInterface;
import com.example.zzapdiz.share.project.MakerType;
import com.example.zzapdiz.share.project.ProjectCategory;
import com.example.zzapdiz.share.project.ProjectType;
import com.example.zzapdiz.share.project.RewardMakeType;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundingProjectService {

    // 인터페이스
    private final MemberExceptionInterface memberExceptionInterface;
    private final FundingProejctExceptionInterface fundingProejctExceptionInterface;
    private final RewardExceptionInterface rewardExceptionInterface;
    private final MediaUploadInterface mediaUploadInterface;

    // Repository
    private final Phase1RedisRepository phase1RedisRepository;
    private final Phase2RedisRepository phase2RedisRepository;
    private final Phase3RedisRepository phase3RedisRepository;
    private final Phase4RedisRepository phase4RedisRepository;
    private final FundingProjectRepository fundingProjectRepository;
    private final RewardRepository rewardRepository;
    private final RewardOptionRepository rewardOptionRepository;

    // 기타 의존성
    private final JwtTokenProvider jwtTokenProvider;
    private final DynamicQueryDsl dynamicQueryDsl;
    private final HashMap<Long, List<RewardCreateResponseDto>> rewards = new HashMap<>();

    // 펀딩 프로젝트 생성 1단계
    public ResponseEntity<ResponseBody> fundingCreatePhase1(
            HttpServletRequest request,
            FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDt0) {

        // 생성하는 유저의 id
        Long memberId = jwtTokenProvider.getMemberFromAuthentication().getMemberId();

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }
        // 1단계 정보들 확인
        if (fundingProejctExceptionInterface.checkPhase1Info(fundingProjectCreatePhase1RequestDt0)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }

        // Redis로 1단계 정보 저장 관리
        FundingProjectCreatePhase1ResponseDto fundingProjectCreatePhase1ResponseDto = FundingProjectCreatePhase1ResponseDto.builder()
                .memberId(memberId)
                .projectCategory(fundingProjectCreatePhase1RequestDt0.getProjectCategory())
                .projectType(fundingProjectCreatePhase1RequestDt0.getProjectType())
                .makerType(fundingProjectCreatePhase1RequestDt0.getMakerType())
                .rewardType(fundingProjectCreatePhase1RequestDt0.getRewardType())
                .rewardMakeType(fundingProjectCreatePhase1RequestDt0.getRewardMakeType())
                .achievedAmount(fundingProjectCreatePhase1RequestDt0.getAchievedAmount())
                .build();

        // 만약 1단계 정보가 저장된 상태에서 다시 저장하려고 할 때 기존 정보 삭제
        if (phase1RedisRepository.findById(memberId).isPresent()) {
            phase1RedisRepository.deleteById(memberId);
        }

        // 1단계 정보 저장
        phase1RedisRepository.save(fundingProjectCreatePhase1ResponseDto);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("createMessage", "펀딩 프로젝트 생성 1단계 완료!");
        resultSet.put("phase1Info", fundingProjectCreatePhase1ResponseDto);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }


    // 펀딩 프로젝트 생성 2단계
    public ResponseEntity<ResponseBody> fundingCreatePhase2(
            HttpServletRequest request,
            FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto,
            MultipartFile thumbnailImage) {

        // 생성하는 유저의 id
        Long memberId = jwtTokenProvider.getMemberFromAuthentication().getMemberId();

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }
        // 2단계 정보 확인
        if (fundingProejctExceptionInterface.checkPhase2Info(fundingProjectCreatePhase2RequestDto, thumbnailImage)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }
        if (fundingProejctExceptionInterface.checkDuplicatedTitle(fundingProjectCreatePhase2RequestDto.getProjectTitle())) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.DUPLICATED_PROJECT_TITLE, null), HttpStatus.OK);
        }

        // Redis로 2단계 정보 저장 관리
        FundingProjectCreatePhase2ResponseDto fundingProjectCreatePhase2ResponseDto = FundingProjectCreatePhase2ResponseDto.builder()
                .memberId(memberId)
                .projectTitle(fundingProjectCreatePhase2RequestDto.getProjectTitle())
                .thumbnailImage(thumbnailImage.getOriginalFilename())
                .endDate(fundingProjectCreatePhase2RequestDto.getEndDate())
                .adultCheck(fundingProjectCreatePhase2RequestDto.getAdultCheck())
                .searchTag(fundingProjectCreatePhase2RequestDto.getSearchTag())
                .build();

        // 이미 2단계 정보를 저장한 상태에서 다시 저장할 경우 기존 정보 삭제
        if (phase2RedisRepository.findById(memberId).isPresent()) {
            dynamicQueryDsl.deleteMedia(thumbnailImage);
            mediaUploadInterface.deleteFile(thumbnailImage.getOriginalFilename());
            phase2RedisRepository.deleteById(memberId);
        }

        // 대표 썸네일 이미지 저장
        mediaUploadInterface.uploadMedia(thumbnailImage, "thumb", fundingProjectCreatePhase2RequestDto.getProjectTitle());
        // 2단계 정보 저장
        phase2RedisRepository.save(fundingProjectCreatePhase2ResponseDto);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("createMessage", "펀딩 프로젝트 생성 2단계 완료!!");
        resultSet.put("phase2Info", fundingProjectCreatePhase2ResponseDto);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }


    // 펀딩 프로젝트 생성 3단계
    public ResponseEntity<ResponseBody> fundingCreatePhase3(
            HttpServletRequest request,
            FundingProjectCreatePhase3RequestDto fundingProjectCreatePhase3RequestDto,
            List<MultipartFile> videoAndImages) {

        // 생성하는 유저의 id
        Long memberId = jwtTokenProvider.getMemberFromAuthentication().getMemberId();

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }
        // 펀딩 프로젝트 3단계 기입 정보들 확인
        if (fundingProejctExceptionInterface.checkPhase3Info(fundingProjectCreatePhase3RequestDto, videoAndImages)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }

        // Redis로 3단계 정보 저장 관리
        FundingProjectCreatePhase3ResponseDto fundingProjectCreatePhase3ResponseDto = FundingProjectCreatePhase3ResponseDto.builder()
                .memberId(memberId)
                .storyText(fundingProjectCreatePhase3RequestDto.getStoryText())
                .projectDescript(fundingProjectCreatePhase3RequestDto.getProjectDescript())
                .openReservation(fundingProjectCreatePhase3RequestDto.getOpenReservation())
                .build();

        // 이미 3단계 정보를 저장한 상태에서 다시 저장할 경우 기존 정보 삭제
        if (phase3RedisRepository.findById(memberId).isPresent()) {
            // s3와 DB에서 미디어 삭제
            for (int i = 0; i < videoAndImages.size(); i++) {
                dynamicQueryDsl.deleteMedia(videoAndImages.get(i));
                mediaUploadInterface.deleteFile(videoAndImages.get(i).getOriginalFilename());
            }
            // redis에 임시저장한 3단계 정보들 삭제
            phase3RedisRepository.deleteById(memberId);
        }

        // 업로드하려고하는 여러 장의 미디어마다 저장
        for (int i = 0; i < videoAndImages.size(); i++) {
            // 대표 썸네일 이미지 저장
            mediaUploadInterface.uploadMedia(videoAndImages.get(i), "story", phase2RedisRepository.findById(memberId).get().getProjectTitle());
        }

        // 생성 정보 저장
        phase3RedisRepository.save(fundingProjectCreatePhase3ResponseDto);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("createMessage", "펀딩 프로젝트 생성 3단계 완료!!!");
        resultSet.put("phase3Info", fundingProjectCreatePhase3ResponseDto);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }


    // 펀딩 프로젝트 생성 4단계
    public ResponseEntity<ResponseBody> fundingCreatePhase4(
            HttpServletRequest request,
            FundingProjectCreatePhase4RequestDto fundingProjectCreatePhase4RequestDto,
            List<RewardCreateRequestDto> rewardCreateRequestDtos) {

        Member authMember = jwtTokenProvider.getMemberFromAuthentication();
        Long memberId = jwtTokenProvider.getMemberFromAuthentication().getMemberId();

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }
        // 펀딩 프로젝트 4단계 기입 정보들 확인
        if (fundingProejctExceptionInterface.checkPhase4Info(fundingProjectCreatePhase4RequestDto)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.EXIST_INCORRECTABLE_FUNDING_INFO, null), HttpStatus.BAD_REQUEST);
        }
        if (rewardExceptionInterface.checkRewardAmount(rewardCreateRequestDtos)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.NEED_AMOUNT_CHECK, null), HttpStatus.BAD_REQUEST);
        }
        if (rewardExceptionInterface.checkRewardInfo(rewardCreateRequestDtos)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.NEED_REWARD_INFO_CHECK, null), HttpStatus.BAD_REQUEST);
        }

        // Redis로 4단계 정보들 저장
        FundingProjectCreatePhase4ResponseDto fundingProjectCreatePhase4ResponseDto = FundingProjectCreatePhase4ResponseDto.builder()
                .memberId(memberId)
                .deliveryCheck(fundingProjectCreatePhase4RequestDto.getDeliveryCheck())
                .deliveryPrice(fundingProejctExceptionInterface.deliveryChecking(fundingProjectCreatePhase4RequestDto.getDeliveryCheck()))
                .deliveryStartDate(fundingProjectCreatePhase4RequestDto.getDeliveryStartDate())
                .build();

        // 이미 4단계 정보를 저장한 상태에서 다시 저장할 경우 기존 정보 삭제
        if (phase4RedisRepository.findById(memberId).isPresent()) {
            phase4RedisRepository.deleteById(memberId);
        }

        phase4RedisRepository.save(fundingProjectCreatePhase4ResponseDto);

        List<RewardCreateResponseDto> rewardCreateResponseDtos = new ArrayList<>();
        int no = 0;

        // 리워드는 여러개를 생성할 수 있다.
        for (RewardCreateRequestDto rewardCreateRequestDto : rewardCreateRequestDtos) {
            // 다시 4단계 정보들을 input 하는 과정에서 리워드도 임시저장된 이전 정보가 존재한다면 삭제 처리
            if (rewards.get(authMember.getMemberId()) != null) {
                rewards.remove(authMember.getMemberId());
            }

            // 리워드 옵션이 존재할 경우 내용 포함 변수
            String optionContent = "";

            // 리워드에 옵션이 필요할 경우 옵션 내용 추가
            if (rewardCreateRequestDto.getRewardOptionOnOff().equals("O")) {
                optionContent = rewardCreateRequestDto.getOptionContent();
            }

            // 리워드 정보들 Redis에 저장 관리
            RewardCreateResponseDto rewardCreateResponseDto = RewardCreateResponseDto.builder()
                    .memberId(memberId)
                    .rewardTitle(rewardCreateRequestDto.getRewardTitle())
                    .rewardContent(rewardCreateRequestDto.getRewardContent())
                    .rewardQuantity(rewardCreateRequestDto.getRewardQuantity())
                    .rewardAmount(rewardCreateRequestDto.getRewardAmount())
                    .rewardOptionOnOff(rewardCreateRequestDto.getRewardOptionOnOff())
                    .optionContent(optionContent)
                    .no(no)
                    .build();

            rewardCreateResponseDtos.add(rewardCreateResponseDto);
            no++;
        }

        rewards.put(authMember.getMemberId(), rewardCreateResponseDtos);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("createMessage", "펀딩 프로젝트 생성 4단계 완료!!!!");
        resultSet.put("phase4Info", fundingProjectCreatePhase4ResponseDto);
        resultSet.put("rewardsInfo", rewardCreateResponseDtos);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }


    // 펀딩 프로젝트 생성 5단계 마지막
    @Transactional
    public ResponseEntity<ResponseBody> fundingCreateFinal(HttpServletRequest request) {

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        // 인증받은 Member 객체 조회
        Member authMember = jwtTokenProvider.getMemberFromAuthentication();
        Long memberId = authMember.getMemberId();

        // Redis로 관리되던 프로젝트 생성 정보들 조회
        Optional<FundingProjectCreatePhase1ResponseDto> phase1 = phase1RedisRepository.findById(memberId);
        Optional<FundingProjectCreatePhase2ResponseDto> phase2 = phase2RedisRepository.findById(memberId);
        Optional<FundingProjectCreatePhase3ResponseDto> phase3 = phase3RedisRepository.findById(memberId);
        Optional<FundingProjectCreatePhase4ResponseDto> phase4 = phase4RedisRepository.findById(memberId);

        if (fundingProejctExceptionInterface.checkAllPhase(phase1, phase2, phase3, phase4)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.TIME_LIMIT_CHECK, null), HttpStatus.BAD_REQUEST);
        }

        // static으로 저장되었던 Reward 객체 정보 조회
        List<RewardCreateResponseDto> rewardsInfo = rewards.get(memberId);

        // 프로젝트 종료일, 프로젝트 시작일, 배송 시작일을 날짜 형식으로 변환하여 반영
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime endDate = LocalDateTime.parse(phase2.get().getEndDate(), formatter);
        LocalDateTime deliveryStartDate = LocalDateTime.parse(phase4.get().getDeliveryStartDate(), formatter);
        LocalDateTime startDate = LocalDateTime.parse(fundingProejctExceptionInterface.startDateSetting(phase3.get().getOpenReservation(), phase3.get().getOpenReservationStartDate()), formatter);

        // FundingProject 객체에 실반영
        FundingProject fundingProject = FundingProject.builder()
                .projectCategory(ProjectCategory.findCategory(phase1.get().getProjectCategory()))
                .projectType(ProjectType.findProjectType(phase1.get().getProjectType()))
                .makerType(MakerType.findMakerType(phase1.get().getMakerType()))
                .achievedAmount(phase1.get().getAchievedAmount())
                .projectTitle(phase2.get().getProjectTitle())
                .endDate(endDate)
                .adultCheck(phase2.get().getAdultCheck())
                .searchTag(phase2.get().getSearchTag())
                .storyText(phase3.get().getStoryText())
                .projectDescript(phase3.get().getProjectDescript())
                .openReservation(phase3.get().getOpenReservation())
                .startDate(startDate)
                .deliveryCheck(phase4.get().getDeliveryCheck())
                .deliveryPrice(phase4.get().getDeliveryPrice())
                .deliveryStartDate(deliveryStartDate)
                .progress("진행중")
                .member(authMember)
                .build();

        fundingProjectRepository.save(fundingProject);

        // Reward 객체에 실반영
        for (RewardCreateResponseDto eachReward : rewardsInfo) {
            Reward reward = Reward.builder()
                    .rewardType(phase1.get().getRewardType())
                    .rewardMakeType(RewardMakeType.findRewardMakeType(phase1.get().getRewardMakeType()))
                    .rewardTitle(eachReward.getRewardTitle())
                    .rewardAmount(eachReward.getRewardAmount())
                    .rewardQuantity(eachReward.getRewardQuantity())
                    .rewardContent(eachReward.getRewardContent())
                    .fundingProject(fundingProject)
                    .build();

            rewardRepository.save(reward);

            // Reward의 옵션이 존재할 경우 RewardOption 엔티티에 해당 정보 실반영
            if (eachReward.getRewardOptionOnOff().equals("O")) {
                RewardOption rewardOption = RewardOption.builder()
                        .optionContent(eachReward.getOptionContent())
                        .reward(reward)
                        .build();

                rewardOptionRepository.save(rewardOption);
            }
        }

        // 업로드한 사진 및 비디오 미디어 자원들을 조회하여 해당 프로젝트의 아이디를 반영하여 연관짖기
        List<Media> mediaList = dynamicQueryDsl.findMedias(fundingProject.getProjectTitle());
        for (Media eachMedia : mediaList) {
            dynamicQueryDsl.updateMedia(eachMedia, fundingProject.getFundingProjectId());
        }

        // 남아있는 임시저장된 Redis 객체들, 프로젝트 생성 정보들 삭제
        phase1RedisRepository.deleteById(memberId);
        phase2RedisRepository.deleteById(memberId);
        phase3RedisRepository.deleteById(memberId);
        phase4RedisRepository.deleteById(memberId);
        // static 변수로 관리되던 Reward 객체 정보 삭제
        rewards.remove(memberId);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "펀딩 프로젝트 생성 5단계 완료!!!!! 프로젝트 무사 생성을 축하드립니다!"), HttpStatus.OK);
    }


    // 생성 중인 리워드 수정
    public ResponseEntity<ResponseBody> rewardUpdate(
            HttpServletRequest request, FundingRewardUpdateRequestDto rewardUpdateRequestDto) {

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        Member authMember = jwtTokenProvider.getMemberFromAuthentication();

        List<RewardCreateResponseDto> rewardInfo = rewards.get(authMember.getMemberId());

        for (RewardCreateResponseDto reward : rewardInfo) {
            if (reward.getNo() == rewardUpdateRequestDto.getNo()) {
                reward.setRewardTitle(rewardUpdateRequestDto.getRewardTitle());
                reward.setRewardAmount(rewardUpdateRequestDto.getRewardAmount());
                reward.setRewardQuantity(rewardUpdateRequestDto.getRewardQuantity());
                reward.setRewardContent(rewardUpdateRequestDto.getRewardContent());
            }
        }

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("updateMessage", "리워드 수정이 완료되었습니다.");
        resultSet.put("rewardInfo", rewards.get(authMember.getMemberId()).get(rewardUpdateRequestDto.getNo()));

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }


    // 생성 중인 리워드 삭제
    public ResponseEntity<ResponseBody> rewardDelete(HttpServletRequest request, int rewardNo) {

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        if (memberExceptionInterface.checkHeaderToken(request)) {
            return new ResponseEntity<>(new ResponseBody(StatusCode.UNAUTHORIZED_TOKEN, null), HttpStatus.BAD_REQUEST);
        }

        Member authMember = jwtTokenProvider.getMemberFromAuthentication();

        List<RewardCreateResponseDto> rewardInfo = rewards.get(authMember.getMemberId());

        for (RewardCreateResponseDto reward : rewardInfo) {
            if (reward.getNo() == rewardNo) {
                rewardInfo.remove(rewardNo);
            }
        }

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("deleteMessage", "리워드 삭제가 완료되었습니다.");
        resultSet.put("rewardInfo", rewardInfo);


        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }


    // 펀딩 프로젝트 조회
    public ResponseEntity<ResponseBody> fundingProjectRead(Long projectId) {

        // 조회하고자 하는 프로젝트
        FundingProject readFundingProject = dynamicQueryDsl.getFundingProject(projectId);
        // 프로젝트 메이커명
        String makerName = readFundingProject.getMember().getMemberName();
        // 조회하고자 하는 프로젝트에 속한 리워드 데이터들
        List<Reward> readRewards = dynamicQueryDsl.getRewards(readFundingProject);
        // 조회하고자 하는 프로젝트에 속한 미디어 데이터들
        List<Media> readMedias = dynamicQueryDsl.getMedias(readFundingProject.getFundingProjectId());
        // 프로젝트 찜한 수
        Long countPick = dynamicQueryDsl.countPick(readFundingProject);
        // 프로젝트 지지 수
        Long countSupport = dynamicQueryDsl.countSupport(readFundingProject);
        // 현재 조회하고있는 프로젝트와 유사한 프로젝트 네개 생성
        List<FundingProject> getSimilarProjects = dynamicQueryDsl.getSimilarFundingProjects(readFundingProject);
        // 현재 조회하고있는 프로젝트 메이커의 다른 프로젝트 4개 생성
        List<FundingProject> getMakersOtherProjects = dynamicQueryDsl.getMakersOtherProjects(readFundingProject.getMember(), readFundingProject.getFundingProjectId());

        // 프로젝트 종료까지 남은 일자 관련 변수
        int projectEndDate = readFundingProject.getEndDate().getDayOfYear();
        int todayDate = LocalDateTime.now().getDayOfYear();

        ProjectReadResponseDto projectReadResponseDto = ProjectReadResponseDto.builder()
                .fundingProject(readFundingProject)
                .remainDate((projectEndDate - todayDate) + "일 남았습니다.")
                .makerName(makerName)
                .rewards(readRewards)
                .medias(readMedias)
                .countPick(countPick)
                .countSupport(countSupport)
                .similarProjects(getSimilarProjects)
                .makersOtherProjects(getMakersOtherProjects)
                .build();

        // 리워드 옵션, 달성율 우선 스킵

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("immediateResultMessage", "임의의 결과값 세트");
        resultSet.put("resultInfo", projectReadResponseDto);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }
}


