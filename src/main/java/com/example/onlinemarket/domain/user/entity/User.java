package com.example.onlinemarket.domain.user.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String userId;
    private String password;
    private String email;
    private String name;
    private String phone;
    private UserRole role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
