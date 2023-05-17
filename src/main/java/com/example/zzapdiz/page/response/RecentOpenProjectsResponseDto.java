package com.example.zzapdiz.page.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class RecentOpenProjectsResponseDto {
    private Long fundingProjectId;
    private String projectTile;
    private String reachPercentage;
    private String thumbnailImage;
}
