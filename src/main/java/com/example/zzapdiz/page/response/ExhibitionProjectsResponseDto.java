package com.example.zzapdiz.page.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ExhibitionProjectsResponseDto {
    private Long fundingProjectId;
    private String projectTitle;
    private String projectCategory;
    private int fundingCount;
    private String thumbnailImage;
}
