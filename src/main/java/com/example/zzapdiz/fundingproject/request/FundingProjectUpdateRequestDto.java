package com.example.zzapdiz.fundingproject.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FundingProjectUpdateRequestDto {
    private Long projectId;
    private String projectTitle;
    private int achievedAmount;
    private String searchTag;
    private String storyText;
    private String projectDescript;
    private String endDate;
    private String deliveryCheck;
    private int deliveryPrice;
    private String deliveryStartDate;
}
