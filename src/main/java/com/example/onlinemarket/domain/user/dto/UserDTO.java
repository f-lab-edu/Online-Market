package com.example.onlinemarket.domain.user.dto;

import com.example.onlinemarket.common.eunm.Role;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {

    private int id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private Role role;
    private Date createdAt;
    private Date updatedAt;

    public UserDTO(int id, String email, String password, String name, String phone, Role role,
            Date createdAt, Date updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
