package com.example.onlinemarket.domain.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryDTO {

  @NotNull
  private Long id;

  @NotNull
  private String name;

  @Builder
  public CategoryDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public void updateCategoryName(String name) {
    this.name = name;
  }
}
