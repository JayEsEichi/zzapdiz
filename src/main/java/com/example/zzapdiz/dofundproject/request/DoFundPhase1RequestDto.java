package com.example.zzapdiz.dofundproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DoFundPhase1RequestDto {
    private Long fundingProjectId;
    private Long rewardId;
}
