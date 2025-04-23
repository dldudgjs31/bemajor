package com.hong.bemajor.login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "로그인 ID는 필수 입력 항목입니다.")
    private String login_id;  // 사용자 아이디

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;  // 사용자 비밀번호
}