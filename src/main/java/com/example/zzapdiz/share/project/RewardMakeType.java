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

    public static String findRewardMakeType(String rewardMakeType){
        switch(rewardMakeType){
            case "SELF_MADE":
                return RewardMakeType.SELF_MADE.rewardMakeType;
            case "CONSIGNMENT_MADE":
                return RewardMakeType.CONSIGNMENT_MADE.rewardMakeType;
            case "ODM":
                return RewardMakeType.ODM.rewardMakeType;
            case "GLOBAL":
                return RewardMakeType.GLOBAL.rewardMakeType;
        }
        return null;
    }
}
