package com.example.onlinemarket.domain.user.dto;

import com.example.onlinemarket.common.eunm.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

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

    private Role role;
    private Date createdAt;
    private Date updatedAt;

    public UserDTO(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
}
