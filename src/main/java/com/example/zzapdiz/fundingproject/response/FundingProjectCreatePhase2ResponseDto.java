package com.example.zzapdiz.fundingproject.response;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Builder
@Setter
@Getter
public class FundingProjectCreatePhase2ResponseDto implements Serializable {
    private String projectTitle;
    private MultipartFile thumbnailImage;
    private String endDate;
    private String adultCheck;
    private String searchTag;
}
