package com.example.zzapdiz.fundingproject.service;

import com.example.zzapdiz.exception.fundingproject.FundingProjectException;
import com.example.zzapdiz.exception.member.MemberException;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase1RequestDto;
import com.example.zzapdiz.fundingproject.request.FundingProjectCreatePhase2RequestDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase1ResponseDto;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase2ResponseDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class FundingProjectService {

    private final MemberException memberException;
    private final FundingProjectException fundingProjectException;
    private final FundingProjectCreatePhase1ResponseDto fundingProjectCreatePhase1ResponseDto;
    private final FundingProjectCreatePhase2ResponseDto fundingProjectCreatePhase2ResponseDto;


    // 펀딩 프로젝트 생성 1단계
    public ResponseEntity<ResponseBody> fundingCreatePhase1(
            HttpServletRequest request,
            FundingProjectCreatePhase1RequestDto fundingProjectCreatePhase1RequestDt0){

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        memberException.checkHeaderToken(request);

        // spring scope bean 을 사용하여 1단계 생성 정보들을 session으로 어플리케이션 내부에 임시저장
        // scale out 해서 이후에 서버를 증설한다면 redis로 이전할 필요가 있음
        fundingProjectCreatePhase1ResponseDto.setProjectCategory(fundingProjectCreatePhase1RequestDt0.getProjectCategory());
        fundingProjectCreatePhase1ResponseDto.setProjectType(fundingProjectCreatePhase1RequestDt0.getProjectType());
        fundingProjectCreatePhase1ResponseDto.setMakerType(fundingProjectCreatePhase1RequestDt0.getMakerType());
        fundingProjectCreatePhase1ResponseDto.setRewardType(fundingProjectCreatePhase1RequestDt0.getRewardType());
        fundingProjectCreatePhase1ResponseDto.setRewardMakeType(fundingProjectCreatePhase1RequestDt0.getRewardMakeType());
        fundingProjectCreatePhase1ResponseDto.setAchievedAmount(fundingProjectCreatePhase1RequestDt0.getAchievedAmount());

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, "펀딩 프로젝트 생성 1단계 완료!"), HttpStatus.OK);
    }


    // 펀딩 프로젝트 생성 2단계
    public ResponseEntity<ResponseBody> fundingCreatePhase2(
            HttpServletRequest request,
            FundingProjectCreatePhase2RequestDto fundingProjectCreatePhase2RequestDto,
            MultipartFile thumbnailImage){

        // 펀딩 프로젝트 생성 요청 회원의 토큰 유효성 검증
        memberException.checkHeaderToken(request);
        // 2단계 정보 확인
        fundingProjectException.checkPhase2Info(fundingProjectCreatePhase2RequestDto, thumbnailImage);

        // sprin scope bean 을 사용하여 2단계 생성 정보들을 session으로 어플리케이션 내부에 저장 및 관리
        fundingProjectCreatePhase2ResponseDto.setProjectTitle(fundingProjectCreatePhase2RequestDto.getProjectTitle());
        fundingProjectCreatePhase2ResponseDto.setThumbnailImage(thumbnailImage);
        fundingProjectCreatePhase2ResponseDto.setEndDate(fundingProjectCreatePhase2RequestDto.getEndDate());
        fundingProjectCreatePhase2ResponseDto.setAdultCheck(fundingProjectCreatePhase2RequestDto.getAdultCheck());
        fundingProjectCreatePhase2ResponseDto.setSearchTag(fundingProjectCreatePhase2RequestDto.getSearchTag());

        HashMap<String, Object> resultSet = new HashMap<>();
        resultSet.put("phase2CreateMessage", "펀딩 프로젝트 생성 2단계 완료!");
        resultSet.put("phase2ResponseInfo", fundingProjectCreatePhase2ResponseDto);

        return new ResponseEntity<>(new ResponseBody(StatusCode.OK, resultSet), HttpStatus.OK);
    }

    // Gson().toJson("Json 객체 혹은 Dto 객체") -> Json 형식의 객체를 String 타입으로 변환
    // Gson().fromJson("String 타입으로 변환된 객체", 변환될 Json 형식객체 혹은 DTO 객체.class) -> String 타입으로 변환된 Json 객체를 다시 Json 형식으로 변환

    // GSON에서 LocalDate 유형의 데이터를 String 타입으로 변환하기 위한 설정
    private Gson gsonConverterFactory() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                })
                .registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
                    @Override
                    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return LocalTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("HH:mm:ss"));
                    }
                })
                .create();

        return gson;
    }
}


