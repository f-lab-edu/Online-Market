package com.example.onlinemarket.domain.user.dto;

import com.example.onlinemarket.common.eunm.Role;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
public class UserDTO {

    private int id;
    private String email;
    private String password;
    private String name;
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