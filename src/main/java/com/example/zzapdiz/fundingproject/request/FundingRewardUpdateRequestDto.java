package com.example.zzapdiz.fundingproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FundingRewardUpdateRequestDto {
    private int no;
    private String rewardTitle;
    private int rewardAmount;
    private int rewardQuantity;
    private String rewardContent;
}
