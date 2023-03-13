package com.example.zzapdiz.fundingproject.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FundingProjectCreatePhase1ResponseDto {
    private String projectCategory;
    private String projectType;
    private String makerType;
    private String rewardType;
    private String rewardMakeType;
    private int achievedAmount;
}
