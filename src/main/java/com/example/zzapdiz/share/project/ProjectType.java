package com.example.zzapdiz.share.project;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ProjectType {

    FIRST_OPEN("첫 출시"),
    REMAKE("기존에 출시된 제품/서비스/컨텐츠를 리메이크"),
    BROAD_SELL("해외에서 출시된 제품 공식 유통"),
    SUCCESSED_PROJECT_REOPEN("와디즈에서 성공한 프로젝트 다시 공유");

    private String projectType;

    public static String findProjectType(String projectType){
        switch (projectType){
            case "FIRST_OPEN":
                return ProjectType.FIRST_OPEN.projectType;
            case "REMAKE":
                return ProjectType.REMAKE.projectType;
            case "BROAD_SELL":
                return ProjectType.BROAD_SELL.projectType;
            case "SUCCESSED_PROJECT_REOPEN":
                return ProjectType.SUCCESSED_PROJECT_REOPEN.projectType;
        }
        return null;
    }
}
