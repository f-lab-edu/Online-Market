package com.example.onlinemarket.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "아이디 입력은 필수입니다.")
    @Size(min = 6, max = 12, message = "아이디는 {min}자리 이상 {max}자 이하여야 합니다.")
    private String userId;

    @NotBlank(message = "패스워드를 입력은 필수입니다.")
    private String password;
}
