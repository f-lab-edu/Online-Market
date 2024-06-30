package com.example.onlinemarket.domain.category.service;

import com.example.onlinemarket.domain.category.dto.CategoryDto;
import com.example.onlinemarket.domain.category.dto.request.CategoryCreateRequest;
import com.example.onlinemarket.domain.category.entity.Category;
import com.example.onlinemarket.domain.category.exception.DuplicatedCategoryNameException;
import com.example.onlinemarket.domain.category.exception.NotFoundCategoryException;
import com.example.onlinemarket.domain.category.mapper.CategoryMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public Long saveCategory(CategoryCreateRequest request) {
        if (isNameExists(request.getName())) {
            throw new DuplicatedCategoryNameException("이미 존재하는 카테고리명입니다.");
        }
        Category category = request.toEntity();
        categoryMapper.insert(category);

        return category.getId();
    }

    public List<CategoryDto> getCategories() {
        List<Category> category = categoryMapper.findAll();

        return category.stream()
            .map(CategoryDto::of)
            .collect(Collectors.toList());
    }

    public CategoryDto findById(Long id) {
        Category category = validateCategory(id);
        return CategoryDto.of(category);
    }

    public void updateCategoryName(Long id, String newName) {
        Category category = validateCategory(id);
        category.updateName(newName);
        categoryMapper.updateName(id, newName);
    }

    public void deleteCategory(Long id) {
        validateCategory(id);
        categoryMapper.deleteById(id);
    }

    private boolean isNameExists(String name) {
        return categoryMapper.existsByName(name);
    }

    private Category validateCategory(Long id) {
        return categoryMapper.findById(id)
            .orElseThrow(() -> new NotFoundCategoryException("해당 카테고리를 찾을 수 없습니다."));
    }
}
