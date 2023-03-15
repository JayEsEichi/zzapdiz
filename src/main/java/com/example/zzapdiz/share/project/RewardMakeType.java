package com.example.zzapdiz.share.project;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum RewardMakeType {

    SELF_MADE("직접 제조"),
    CONSIGNMENT_MADE("위탁 제조"),
    ODM("ODM"),
    GLOBAL("글로벌");

    private String rewardMakeType;
}
