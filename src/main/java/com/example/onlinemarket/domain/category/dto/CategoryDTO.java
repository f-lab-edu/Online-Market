package com.example.onlinemarket.domain.category.dto;

import com.example.onlinemarket.domain.category.entity.Category;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static CategoryDto of(Category category) {
        return CategoryDto.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }
}
