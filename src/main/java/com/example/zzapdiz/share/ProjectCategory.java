package com.example.zzapdiz.share;

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

}
