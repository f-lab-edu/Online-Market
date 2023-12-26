package com.example.onlinemarket.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class UserDTO {

    public enum Role {
        USER, ADMIN
    }

    private int id;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 양식을 지켜주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10-11 자리의 전화번호를 입력해야 해요.")
    private String phone;

    @NotNull(message = "역할을 선택해야 합니다.")
    private Role role;

    private Date createdAt;
    private Date updatedAt;

    public UserDTO(String email, String password, String name, String phone, Role role,
        Date createdAt, Date updatedAt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static boolean hasNullDataBeforeSignup(UserDTO userDTO) {
        return userDTO.getEmail() == null || userDTO.getPassword() == null
            || userDTO.getName() == null;
    }

}
