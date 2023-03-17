package com.example.zzapdiz.exception.reward;

import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RewardExceptionInterface {

    /** 리워드 생성 시 수량 체크 **/
    ResponseEntity<ResponseBody> rewardAmountCheck(List<RewardCreateRequestDto> rewardCreateRequestDtos);
}
