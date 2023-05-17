package com.example.zzapdiz.page.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SuitableProjectsResponseDto {
    private Long fundingProjectId;
    private String projectTitle;
    private int fundingCount;
    private String projectCategory;
    private String thumbnailImage;
}
