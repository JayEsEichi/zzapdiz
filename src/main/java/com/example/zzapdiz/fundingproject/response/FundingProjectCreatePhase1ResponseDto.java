package com.example.zzapdiz.fundingproject.response;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Component
@RedisHash(value = "phase1ResponseDto", timeToLive = 1800)
@Builder
@Setter
@Getter
public class FundingProjectCreatePhase1ResponseDto implements Serializable {
    @Id
    private Long memberId;
    private String projectCategory;
    private String projectType;
    private String makerType;
    private String rewardType;
    private String rewardMakeType;
    private int achievedAmount;

}
