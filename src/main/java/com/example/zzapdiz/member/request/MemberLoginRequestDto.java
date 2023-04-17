package com.example.zzapdiz.member.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberLoginRequestDto {

    @NotBlank(message = "아이디는 공백을 포함할 수 없습니다.")
    @Email(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+.[a-z]+${4,12}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotNull(message = "비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{7,20}",
            message = "비밀번호는 영어 대/소문자, 숫자, 특수기호가 최소한 1개씩은 포함이 되어있어야 하며, 7~20글자 이내여야 합니다.")
    private String password;
}
