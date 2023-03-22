package com.example.zzapdiz.pickproject.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PickProjectResponseDto {
    private Long pickProjectId;
    private Long memberId;
    private Long fundingProjectId;
}
