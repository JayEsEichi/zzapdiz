package com.example.zzapdiz.fundingproject.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectsReadResponseDto {
    private Long fundingProjectId;
    private String projectTitle;
    private String reachPercentage;
    private String reachQuantity;
    private String remainProjectDate;
    private String thumbnailImage;
}
