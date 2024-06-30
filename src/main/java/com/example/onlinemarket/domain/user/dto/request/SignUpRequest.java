package com.example.onlinemarket.domain.user.dto.request;

import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "아이디 입력은 필수입니다.")
    @Size(min = 6, max = 12, message = "아이디는 {min}자리 이상 {max}자 이하여야 합니다.")
    private String userId;

    @NotBlank(message = "패스워드 입력은 필수입니다.")
    private String password;

    @NotBlank(message = "이메일 입력은 필수입니다.")
    @Email(message = "이메일 형식에 맞게 입력해 주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "휴대펀 번호 입력은 필수입니다.")
    @Pattern(regexp = "^(010)[0-9]{8}$", message = "휴대폰 번호 형식에 맞게 입력해 주세요.")
    private String phone;

    public User toEntity(String encryptedPassword) {
        return User.builder()
            .userId(userId)
            .password(encryptedPassword)
            .email(email)
            .name(name)
            .phone(phone)
            .role(UserRole.USER)
            .createdDate(LocalDateTime.now())
            .updatedDate(null)
            .build();
    }
}
