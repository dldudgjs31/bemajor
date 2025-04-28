package com.hong.bemajor.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private Long member_id;

    @NotBlank(message = "로그인 ID는 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{1,20}$",
            message = "로그인 ID는 알파벳 대소문자와 숫자만 허용되며, 20자 이내여야 합니다."
    )
    private String login_id;  // 아이디는 알파벳 대소문자와 숫자만 허용, 20자 이내


    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*[a-zA-Z0-9]).{8,20}$",
            message = "비밀번호는 8~20자 이내여야 하며, 최소 1개의 특수문자가 포함되어야 합니다."
    )
    private String password;  // 비밀번호는 8~20자, 최소 1개의 특수문자 포함

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @Email(message = "이메일 형식이 유효하지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;  // 이메일은 유효한 형식이어야 하며, 빈 값이 허용되지 않습니다.

    // yyyyMMdd 형식(8자리 숫자) 검증
    @Pattern(
            regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
            message = "유효기간 시작일은 yyyyMMdd 형식이어야 합니다."
    )
    private String valid_from;

    @Pattern(
            regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
            message = "유효기간 종료일은 yyyyMMdd 형식이어야 합니다."
    )
    private String valid_to;

    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
    private String phone;

    private String address;

    private String branch_id;

    @Pattern(
            regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
            message = "유효기간 종료일은 yyyyMMdd 형식이어야 합니다."
    )
    private String birth_date;
    private String rank_id;


}
