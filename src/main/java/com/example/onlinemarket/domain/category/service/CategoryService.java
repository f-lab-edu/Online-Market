package com.example.onlinemarket.domain.category.service;

import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.domain.category.dto.CategoryDTO;
import com.example.onlinemarket.domain.category.mapper.CategoryMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

@AllArgsConstructor
@Transactional
public class CategoryService {

  @Autowired
  private final CategoryMapper categoryMapper;

  public List<CategoryDTO> getCategories() {

    return categoryMapper.findAll();
  }

  public CategoryDTO findById(Long id) {

    CategoryDTO categoryId = categoryMapper.findById(id);

    if (categoryId == null) {
      throw new NotFoundException();
    }
    return categoryId;
  }

  public Long saveCategory(CategoryDTO categoryDTO) {

    categoryMapper.insert(categoryDTO);

    return categoryDTO.getId();
  }

  public void updateCategoryName(Long id, CategoryDTO categoryDTO) {

    CategoryDTO categoryId = categoryMapper.findById(id);

    if (categoryId == null) {
      throw new NotFoundException();
    }

    categoryDTO.updateCategoryName(categoryDTO.getName());
    categoryMapper.updateName(categoryDTO);
  }

  public void deleteCategory(Long id) {
    categoryMapper.deleteById(id);
  }
}
