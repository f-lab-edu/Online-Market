package com.example.onlinemarket.domain.category.dto.request;

import com.example.onlinemarket.domain.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest {

    @NotBlank(message = "카테고리명을 입력해주세요.")
    private String name;

    public Category toEntity() {
        return Category.builder()
            .name(name)
            .build();
    }
}


