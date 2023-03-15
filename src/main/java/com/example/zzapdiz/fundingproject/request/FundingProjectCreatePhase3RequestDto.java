package com.example.zzapdiz.fundingproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FundingProjectCreatePhase3RequestDto {

    private String storyText;
    private String projectDescript;
    private String openReservation;

}
