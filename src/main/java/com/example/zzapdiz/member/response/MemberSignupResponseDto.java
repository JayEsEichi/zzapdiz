package com.example.zzapdiz.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberSignupResponseDto {
    private String email;
    private String password;
    private String memberName;
    private int point;
}
