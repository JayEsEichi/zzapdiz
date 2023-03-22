package com.example.zzapdiz.exception.reward;

import com.example.zzapdiz.reward.request.RewardCreateRequestDto;
import com.example.zzapdiz.share.ResponseBody;
import com.example.zzapdiz.share.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RewardException implements RewardExceptionInterface {

    // 리워드 생성 시 수량 체크
    public Boolean checkRewardAmount(List<RewardCreateRequestDto> rewardCreateRequestDtos) {
        for (RewardCreateRequestDto rewardCreateRequestDto : rewardCreateRequestDtos) {
            if (rewardCreateRequestDto.getRewardAmount() > 0 && rewardCreateRequestDto.getRewardAmount() < 50) {
                return true;
            }
        }

        return false;
    }


    // 리워드들 정보 확인
    @Override
    public Boolean checkRewardInfo(List<RewardCreateRequestDto> rewardCreateRequestDtos) {
        for (RewardCreateRequestDto reward : rewardCreateRequestDtos) {
            if (reward.getRewardAmount() == 0 ||
                    reward.getRewardContent() == null ||
                    reward.getRewardTitle() == null ||
                    reward.getRewardQuantity() == 0) {
                return true;
            }
        }

        return false;
    }

}
