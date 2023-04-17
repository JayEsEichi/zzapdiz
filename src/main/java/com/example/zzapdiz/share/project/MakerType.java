package com.example.zzapdiz.share.project;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum MakerType {

    PERSONAL("개인"),
    PERSONAL_BUSINESS("개인 사업자"),
    CORPORATE_BUSINESS("법인 사업자");

    private String makerType;

    public static String findMakerType(String makerType){
        switch(makerType){
            case "PERSONAL":
                return MakerType.PERSONAL.makerType;
            case "PERSONAL_BUSINESS":
                return MakerType.PERSONAL_BUSINESS.makerType;
            case "CORPORATE_BUSINESS":
                return MakerType.CORPORATE_BUSINESS.makerType;
        }

        return null;
    }
}
