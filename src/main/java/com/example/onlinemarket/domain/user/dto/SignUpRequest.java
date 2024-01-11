package com.example.onlinemarket.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class SignUpRequest {

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식에 맞게 입력해 주세요.")
    private String email;

    @NotBlank(message = "패스워드를 입력은 필수입니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "휴대펀 번호 입력은 필수입니다.")
    @Pattern(regexp = "^(010)[0-9]{8}$", message = "휴대폰 번호 형식에 맞게 입력해 주세요.")
    private String phone;


    public SignUpRequest(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public UserDTO toEntity(String encryptedPassword) {

        return UserDTO.builder()
                .email(email)
                .password(encryptedPassword)
                .name(name)
                .phone(phone)
                .build();
    }
}
