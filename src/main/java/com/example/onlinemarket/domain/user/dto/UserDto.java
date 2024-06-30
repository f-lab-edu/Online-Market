package com.example.onlinemarket.domain.user.dto;

import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.entity.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String userId;
    private String password;
    private String email;
    private String name;
    private String phone;
    private UserRole role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static UserDto of(User user) {
        return UserDto.builder()
            .id(user.getId())
            .userId(user.getUserId())
            .email(user.getEmail())
            .name(user.getName())
            .phone(user.getPhone())
            .role(user.getRole())
            .createdDate(user.getCreatedDate())
            .updatedDate(user.getUpdatedDate())
            .build();
    }
}
