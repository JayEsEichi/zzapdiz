package com.example.zzapdiz.reward.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RewardCreateRequestDto {
    private String rewardTitle;
    private String rewardContent;
    private int rewardQuantity;
    private int rewardAmount;
    private String rewardOptionOnOff;
    private String optionContent;
}
