package com.example.zzapdiz.fundingproject.response;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Component
@RedisHash(value = "phase2ResponseDto", timeToLive = 1800)
@Builder
@Setter
@Getter
public class FundingProjectCreatePhase2ResponseDto implements Serializable {
    @Id
    private Long memberId;
    private String projectTitle;
    private String thumbnailImage;
    private String endDate;
    private String adultCheck;
    private String searchTag;
}
