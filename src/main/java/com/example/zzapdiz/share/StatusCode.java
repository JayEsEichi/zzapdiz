package com.example.zzapdiz.share;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
public enum StatusCode {

    // 정상 응답
    OK(200, "정상 처리"),


    // 잘못된 요청으로 인한 응답 (클라이언트 입장)
    NOT_MATCH_PASSWORD(452, "비밀번호와 재확인 비밀번호가 일치하지 않습니다."),
    ALREADY_EXIST_MEMBER(453, "이미 존재하는 이메일 계정입니다."),
    EXIST_INCORRECTABLE_DATA(454, "회원가입 정보가 옳바르지 않습니다."),
    NOT_FOUND_MATCHING_EMAIL(455, "입력한 이메일에 맞는 계정 정보가 존재하지 않습니다."),
    DUPLICATED_ACCOUNT(456, "이미 회원가입된 이메일 계정입니다."),
    NOT_FOUND_MATCHING_PASSWORD(457, "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_TOKEN(458, "유효한 토큰이 아닙니다."),
    EXIST_INCORRECTABLE_FUNDING_INFO(459, "펀딩 프로젝트 생성 기입 정보가 올바르지 않습니다."),
    NEED_AMOUNT_CHECK(460, "리워드 수량 설정이 옳지 않습니다. 최소 50개 이상 설정이 필요합니다."),
    DUPLICATED_PROJECT_TITLE(461, "이미 존재하는 프로젝트 타이틀입니다."),
    NEED_REWARD_INFO_CHECK(462, "리워드 정보가 올바르지 않습니다."),
    TIME_LIMIT_CHECK(463, "일정 시간동안 프로젝트를 생성하지 않아 정보가 초기화되었습니다. 다시 단계 정보를 넣어주십시오.");



    // 잘못된 서버 응답 (서버 입장)

    private int statusCode;
    private String statusMessage;

    public int getStatusCode(){
        return statusCode;
    }

    public String getStatusMessage(){
        return statusMessage;
    }
}
