package com.example.zzapdiz.share.project;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ProjectCategory {

    TECH("테크/가전"),
    FOOD("푸드"),
    HOME_LIVING("홈/리빙"),
    SPORTS_MOBILITY("스포츠/모빌리티"),
    CHARACTER_GOODS("캐릭터/굿즈"),
    GAME_HOBBY("게임/취미"),
    CULTURE_ARTIST("컬쳐/아티스트"),
    GROUP("모임");

    private String category;

    public static String findCategory(String projectCatgory){
        switch(projectCatgory){
            case "TECH":
                return ProjectCategory.TECH.category;
            case "FOOD":
                return ProjectCategory.FOOD.category;
            case "HOME_LIVING":
                return ProjectCategory.HOME_LIVING.category;
            case "SPORTS_MOBILITY":
                return ProjectCategory.SPORTS_MOBILITY.category;
            case "CHARACTER_GOODS":
                return ProjectCategory.CHARACTER_GOODS.category;
            case "GAME_HOBBY":
                return ProjectCategory.GAME_HOBBY.category;
            case "CULTURE_ARTIST":
                return ProjectCategory.CULTURE_ARTIST.category;
            case "GROUP":
                return ProjectCategory.GROUP.category;
        }
        return null;
    }

}
