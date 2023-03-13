package com.example.zzapdiz.fundingproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FundingProjectCreatePhase1RequestDto {

    private String projectCategory;
    private String projectType;
    private String makerType;
    private String rewardType;
    private String rewardMakeType;
    private int achievedAmount;
}
