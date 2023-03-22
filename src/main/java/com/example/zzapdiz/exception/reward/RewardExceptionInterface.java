package com.example.zzapdiz.exception.reward;

import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RewardExceptionInterface {

    /** 리워드 생성 시 수량 체크 **/
    Boolean checkRewardAmount(List<RewardCreateRequestDto> rewardCreateRequestDtos);

    /** 리워드들 정보 확인 **/
    Boolean checkRewardInfo(List<RewardCreateRequestDto> rewardCreateRequestDtos);
}
