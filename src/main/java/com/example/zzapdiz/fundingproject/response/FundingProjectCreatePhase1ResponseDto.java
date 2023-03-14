package com.example.zzapdiz.fundingproject.response;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

//@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Builder
@Setter
@Getter
public class FundingProjectCreatePhase1ResponseDto implements Serializable {
    // Serializable 을 implements 한 이유 : 이후에 redis에 임시저장하려면 직렬화가 되어있어야 하기 때문이다.

    private String projectCategory;
    private String projectType;
    private String makerType;
    private String rewardType;
    private String rewardMakeType;
    private int achievedAmount;
}
