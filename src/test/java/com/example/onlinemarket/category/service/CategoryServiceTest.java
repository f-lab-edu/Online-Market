package com.example.onlinemarket.category.service;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.domain.category.dto.CategoryDTO;
import com.example.onlinemarket.domain.category.mapper.CategoryMapper;
import com.example.onlinemarket.domain.category.service.CategoryService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryService categoryService;

  private CategoryDTO categoryDTO;

  @BeforeEach
  void setUp() {
    categoryDTO = new CategoryDTO(1L, "TestCategory");
  }

  @Test
  @DisplayName("전체 카테고리 조회 성공")
  void testGetCategoriesSuccess() {

    when(categoryMapper.findAll()).thenReturn(Arrays.asList(categoryDTO));

    List<CategoryDTO> result = categoryService.getCategories();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(categoryDTO, result.get(0));
  }

  @Test
  @DisplayName("특정 카테고리 조회 성공")
  void testFindByIdSuccess() {

    when(categoryMapper.findById(1L)).thenReturn(categoryDTO);

    CategoryDTO result = categoryService.findById(1L);

    assertNotNull(result);
    assertEquals(categoryDTO, result);
  }

  @Test
  @DisplayName("특정 카테고리 조회 실패 - 카테고리가 존재하지 않음")
  void testFindByIdFailure() {

    when(categoryMapper.findById(1L)).thenReturn(null);

    assertThrows(NotFoundException.class, () -> {
      categoryService.findById(1L);
    });
  }

  @Test
  @DisplayName("카테고리 생성 성공")
  void testSaveCategorySuccess() {

    doNothing().when(categoryMapper).insert(any(CategoryDTO.class));

    Long id = categoryService.saveCategory(categoryDTO);

    assertNotNull(id);
    assertEquals(1L, id);
  }

  @Test
  @DisplayName("카테고리 이름 수정 성공")
  void testUpdateCategoryNameSuccess() {

    when(categoryMapper.findById(1L)).thenReturn(categoryDTO);

    doNothing().when(categoryMapper).updateName(ArgumentMatchers.any(CategoryDTO.class));

    assertDoesNotThrow(() -> categoryService.updateCategoryName(1L, categoryDTO));
  }

  @Test
  @DisplayName("카테고리 이름 수정 실패 - 카테고리가 존재하지 않음")
  void testUpdateCategoryNameFailure() {

    when(categoryMapper.findById(1L)).thenReturn(null);

    assertThrows(NotFoundException.class, () -> {
      categoryService.updateCategoryName(1L, categoryDTO);
    });
  }

  @Test
  @DisplayName("카테고리 삭제 성공")
  void testDeleteCategorySuccess() {

    doNothing().when(categoryMapper).deleteById(1L);

    assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
  }
}
