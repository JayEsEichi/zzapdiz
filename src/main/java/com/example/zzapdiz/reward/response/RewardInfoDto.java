package com.example.zzapdiz.reward.response;

import lombok.*;

@Builder
@Getter
public class RewardInfoDto{
    private int rewardQuantity;
    private String rewardTitle;
    private String rewardContent;
    private String rewardOptionOnOff;
    private String optionContent;
    private int rewardAmount;
}
