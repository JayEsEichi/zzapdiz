package com.example.zzapdiz.dofundproject.response;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FundingRewardResponseDto{
    private Long memberId;
    private Long fundingProjectId;
    private Long rewardId;
}
