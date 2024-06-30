package com.example.onlinemarket.category.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.domain.category.dto.CategoryDto;
import com.example.onlinemarket.domain.category.dto.request.CategoryCreateRequest;
import com.example.onlinemarket.domain.category.entity.Category;
import com.example.onlinemarket.domain.category.exception.NotFoundCategoryException;
import com.example.onlinemarket.domain.category.mapper.CategoryMapper;
import com.example.onlinemarket.domain.category.service.CategoryService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryDto categoryDTO;


    private Category category;


    @BeforeEach
    void setUp() {
        categoryDTO = CategoryDto.builder()
            .id(1L)
            .name("CategoryName")
            .build();

        category = Category.builder()
            .id(1L)
            .name("CategoryName")
            .build();
    }

    @Test
    @DisplayName("전체 카테고리 조회 성공")
    void testGetCategoriesSuccess() {

        when(categoryMapper.findAll()).thenReturn(Arrays.asList(category));

        List<CategoryDto> result = categoryService.getCategories();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(categoryDTO, result.get(0));
    }

    @Test
    @DisplayName("특정 카테고리 조회 성공")
    void testFindByIdSuccess() {

        when(categoryMapper.findById(1L)).thenReturn(null);

        CategoryDto result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals(categoryDTO, result);
    }

    @Test
    @DisplayName("특정 카테고리 조회 실패 - 카테고리가 존재하지 않음")
    void testFindByIdFailure() {

        when(categoryMapper.findById(1L)).thenReturn(null);

        assertThrows(NotFoundCategoryException.class, () -> {
            categoryService.findById(1L);
        });
    }

    @Test
    @DisplayName("카테고리 생성 성공")
    void testSaveCategorySuccess() {
        when(categoryMapper.existsByName(category.getName())).thenReturn(false);
        doNothing().when(categoryMapper).insert(any(Category.class));

        CategoryCreateRequest createRequest = new CategoryCreateRequest(category.getName());

        Long id = categoryService.saveCategory(createRequest);

        assertNotNull(id);
    }

    @Test
    @DisplayName("카테고리 이름 수정 성공")
    void testUpdateCategoryNameSuccess() {
        when(categoryMapper.findById(1L)).thenReturn(null);
        doNothing().when(categoryMapper).updateName(1L, "name");

        assertDoesNotThrow(() -> categoryService.updateCategoryName(1L, "name"));
    }


    @Test
    @DisplayName("카테고리 이름 수정 실패 - 카테고리가 존재하지 않음")
    void testUpdateCategoryNameFailure() {

        when(categoryMapper.findById(1L)).thenReturn(null);

        assertThrows(NotFoundCategoryException.class, () -> {
            categoryService.updateCategoryName(1L, null);
        });
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void testDeleteCategorySuccess() {

        doNothing().when(categoryMapper).deleteById(1L);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
    }
}
