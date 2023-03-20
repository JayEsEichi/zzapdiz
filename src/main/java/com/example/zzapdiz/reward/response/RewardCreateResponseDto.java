package com.example.zzapdiz.reward.response;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Component
@RedisHash(value = "rewardResponseDto", timeToLive = 1800)
@Builder
@Setter
@Getter
public class RewardCreateResponseDto implements Serializable {
    @Id
    private Long memberId;
    private int rewardQuantity;
    private String rewardTitle;
    private String rewardContent;
    private String rewardOptionOnOff;
    private int rewardAmount;
    private String optionContent;
    private int no;
}
