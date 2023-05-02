package com.example.zzapdiz.dofundproject.response;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Component
@RedisHash(value = "doFundPhase1ResponseDto", timeToLive = 1800)
@Builder
@Setter
@Getter
public class DoFundPhase1ResponseDto implements Serializable {

    @Id
    private Long memberId;
    private Long fundingProjectId;
    private Long rewardId;
}
