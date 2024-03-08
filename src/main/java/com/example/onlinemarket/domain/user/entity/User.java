package com.example.onlinemarket.domain.user.entity;

import com.example.onlinemarket.common.eunm.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
public class User {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
