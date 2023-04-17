package com.example.zzapdiz.fundingproject.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectsListReadRequestDto {
    private String projectCategory;
    private String progress;
    private String orderBy;
    private Integer pageNum;
}
