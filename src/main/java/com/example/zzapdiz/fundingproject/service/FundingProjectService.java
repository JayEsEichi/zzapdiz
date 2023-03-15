package com.example.zzapdiz.fundingproject.service;

import com.example.zzapdiz.exception.fundingproject.FundingProjectException;
import com.example.zzapdiz.exception.member.MemberException;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase3RequestDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase1ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase2ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase3ResponseDto;
import com.example.zzapdiz.jwt.JwtTokenProvider;
import com.example.zzapdiz.share.media.MediaRepository;
import com.example.zzapdiz.share.media.MediaUpload;
import com.example.zzapdiz.share.query.DynamicQueryDsl;
import com.example.zzapdiz.share.redis.Phase1RedisRepository;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.example.zzapdiz.share.redis.Phase2RedisRepository;
import com.example.zzapdiz.share.redis.Phase3RedisRepository;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundingProjectService {

    private final MemberException memberException;
    private final FundingProjectException fundingProjectException;
    private final JwtTokenProvider jwtTokenProvider;
    private final Phase1RedisRepository phase1RedisRepository;
    private final Phase2RedisRepository phase2RedisRepository;
    private final Phase3RedisRepository phase3RedisRepository;
    private final MediaRepository mediaRepository;
    private final DynamicQueryDsl dynamicQueryDsl;
    private final MediaUpload mediaUpload;


    // 펀딩 프로젝트 생성 1단계
    public ResponseEntity<ResponseBody> fundingCreatePhase1(
            HttpServletRequest request,
            FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDt0) {

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        memberException.checkHeaderToken(request);
        // 1단계 정보들 확인
        fundingProjectException.checkPhase1Info(fundingProjectCreatePhase1RequestDt0);

        // Redis로 1단계 정보 저장 관리
        FundingProjectCreatePhase1ResponseDto fundingProjectCreatePhase1ResponseDto = FundingProjectCreatePhase1ResponseDto.builder()
                .memberId(jwtTokenProvider.getMemberFromAuthentication().getMemberId())
                .projectCategory(fundingProjectCreatePhase1RequestDt0.getProjectCategory())
                .projectType(fundingProjectCreatePhase1RequestDt0.getProjectType())
                .makerType(fundingProjectCreatePhase1RequestDt0.getMakerType())
                .rewardType(fundingProjectCreatePhase1RequestDt0.getRewardType())
                .rewardMakeType(fundingProjectCreatePhase1RequestDt0.getRewardMakeType())
                .achievedAmount(fundingProjectCreatePhase1RequestDt0.getAchievedAmount())
                .build();

        // 만약 1단계 정보가 저장된 상태에서 다시 저장하려고 할 때 기존 정보 삭제
        if(phase1RedisRepository.findById(jwtTokenProvider.getMemberFromAuthentication().getMemberId()).isPresent()){
            phase1RedisRepository.delete(fundingProjectCreatePhase1ResponseDto);
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

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        memberException.checkHeaderToken(request);
        // 2단계 정보 확인
        fundingProjectException.checkPhase2Info(fundingProjectCreatePhase2RequestDto, thumbnailImage);

        // Redis로 2단계 정보 저장 관리
        FundingProjectCreatePhase2ResponseDto fundingProjectCreatePhase2ResponseDto = FundingProjectCreatePhase2ResponseDto.builder()
                .memberId(jwtTokenProvider.getMemberFromAuthentication().getMemberId())
                .projectTitle(fundingProjectCreatePhase2RequestDto.getProjectTitle())
                .thumbnailImage(thumbnailImage.getOriginalFilename())
                .endDate(fundingProjectCreatePhase2RequestDto.getEndDate())
                .adultCheck(fundingProjectCreatePhase2RequestDto.getAdultCheck())
                .searchTag(fundingProjectCreatePhase2RequestDto.getSearchTag())
                .build();

        // 이미 2단계 정보를 저장한 상태에서 다시 저장할 경우 기존 정보 삭제
        if(phase2RedisRepository.findById(jwtTokenProvider.getMemberFromAuthentication().getMemberId()).isPresent()){
            dynamicQueryDsl.deleteMedia(thumbnailImage);
            mediaUpload.deleteFile(thumbnailImage.getOriginalFilename());
            phase2RedisRepository.delete(fundingProjectCreatePhase2ResponseDto);
        }

        // 대표 썸네일 이미지 저장
        mediaUpload.uploadMedia(thumbnailImage, "thumb");
        // 2단계 정보 저장
        phase2RedisRepository.save(fundingProjectCreatePhase2ResponseDto);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("createMessage", "펀딩 프로젝트 생성 2단계 완료!");
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
        memberException.checkHeaderToken(request);
        // 펀딩 프로젝트 3단계 기입 정보들 확인
        fundingProjectException.checkPhase3Info(fundingProjectCreatePhase3RequestDto, videoAndImages);

        // Redis로 3단계 정보 저장 관리
        FundingProjectCreatePhase3ResponseDto fundingProjectCreatePhase3ResponseDto = FundingProjectCreatePhase3ResponseDto.builder()
                .memberId(memberId)
                .storyText(fundingProjectCreatePhase3RequestDto.getStoryText())
                .projectDescript(fundingProjectCreatePhase3RequestDto.getProjectDescript())
                .openReservation(fundingProjectCreatePhase3RequestDto.getOpenReservation())
                .build();

        // 이미 3단계 정보를 저장한 상태에서 다시 저장할 경우 기존 정보 삭제
        if(phase3RedisRepository.findById(jwtTokenProvider.getMemberFromAuthentication().getMemberId()).isPresent()){
            // s3와 DB에서 미디어 삭제
            for(int i = 0 ; i < videoAndImages.size() ; i++){
                dynamicQueryDsl.deleteMedia(videoAndImages.get(i));
                mediaUpload.deleteFile(videoAndImages.get(i).getOriginalFilename());
            }
            // redis에 임시저장한 3단계 정보들 삭제
            phase3RedisRepository.delete(fundingProjectCreatePhase3ResponseDto);
        }

        // 업로드하려고하는 여러 장의 미디어마다 저장
        for(int i = 0 ; i < videoAndImages.size() ; i++){
            // 대표 썸네일 이미지 저장
            mediaUpload.uploadMedia(videoAndImages.get(i), "story");
        }

        // 생성 정보 저장
        phase3RedisRepository.save(fundingProjectCreatePhase3ResponseDto);

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("createMessage", "펀딩 프로젝트 생성 3단계 완료!!");
        resultSet.put("phase3Info", fundingProjectCreatePhase3ResponseDto);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }


    // Gson().toJson("Json 객체 혹은 Dto 객체") -> Json 형식의 객체를 String 타입으로 변환
    // Gson().fromJson("String 타입으로 변환된 객체", 변환될 Json 형식객체 혹은 DTO 객체.class) -> String 타입으로 변환된 Json 객체를 다시 Json 형식으로 변환

}


