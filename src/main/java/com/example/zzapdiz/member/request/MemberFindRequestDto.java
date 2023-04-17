package com.example.zzapdiz.member.request;

import lombok.Getter;

@Getter
public class MemberFindRequestDto {
    private Long memberId;
    private String email;
    private String password;
    private String memberName;
}
