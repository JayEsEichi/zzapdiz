package com.example.zzapdiz.fundingproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FundingProjectCreatePhase4RequestDto {
    private String deliveryCheck;
    private String deliveryStartDate;
}
