package com.example.zzapdiz.page.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LankingProjectsResponseDto {
    private Long fundingProjectId;
    private String reachPercentage;
    private String projectCategory;
}
