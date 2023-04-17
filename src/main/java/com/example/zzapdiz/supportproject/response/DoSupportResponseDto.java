package com.example.zzapdiz.supportproject.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoSupportResponseDto {
    private Long doSupportId;
    private Long memberId;
    private Long fundingProjectId;
}
