package com.example.onlinemarket.domain.category.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public void updateName(String name) {
        this.name = name;
    }
}
