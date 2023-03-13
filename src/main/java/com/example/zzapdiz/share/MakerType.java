package com.example.zzapdiz.share;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum MakerType {

    PERSONAL("개인"),
    PERSONAL_BUSINESS("개인 사업자"),
    CORPORATE_BUSINESS("법인 사업자");

    private String makerType;
}
