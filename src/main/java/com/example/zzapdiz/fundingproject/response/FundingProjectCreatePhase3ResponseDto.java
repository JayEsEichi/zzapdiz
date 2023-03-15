package com.example.zzapdiz.fundingproject.response;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Component
@RedisHash(value = "phase3ResponseDto", timeToLive = 1800)
@Builder
@Setter
@Getter
public class FundingProjectCreatePhase3ResponseDto implements Serializable {
    @Id
    private Long memberId;
    private String storyText;
    private String projectDescript;
    private String openReservation;
}
