package com.example.zzapdiz.fundingproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FundingProjectCreatePhase2RequestDto {

    private String projectTitle;
    private String endDate;
    private String adultCheck;
    private String searchTag;

}
