package com.example.zzapdiz.dofundproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DoFundPhase2RequestDto {
    private Long couponId;
    private Integer point;
    private String phoneNumber;
    private String address;
    private Integer donation;
}
