package com.example.onlinemarket.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class LoginRequest {

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식에 맞게 입력해 주세요.")
    private String email;

    @NotBlank(message = "패스워드를 입력은 필수입니다.")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
