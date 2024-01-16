package com.example.onlinemarket.category.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.domain.category.controller.CategoryController;
import com.example.onlinemarket.domain.category.dto.CategoryDTO;
import com.example.onlinemarket.domain.category.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CategoryService categoryService;

  private CategoryDTO categoryDTO;

  @BeforeEach
  void setUp() {

    categoryDTO = new CategoryDTO(1L, "CategoryName");
  }

  @Test
  @DisplayName("전체 카테고리 조회 성공")
  void getAllCategories_Success() throws Exception {

    List<CategoryDTO> categoryList = Arrays.asList(categoryDTO);

    given(categoryService.getCategories()).willReturn(categoryList);

    mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is(categoryDTO.getName())));
  }

  @Test
  @DisplayName("특정 카테고리 조회 성공")
  void getCategoryById_Success() throws Exception {

    given(categoryService.findById(1L)).willReturn(categoryDTO);

    mockMvc.perform(MockMvcRequestBuilders.get("/categories/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(categoryDTO.getName())));
  }

  @Test
  @DisplayName("특정 카테고리 조회 실패 - 존재하지 않는 ID")
  void getCategoryById_Fail_NotFound() throws Exception {

    given(categoryService.findById(1L)).willThrow(new NotFoundException("카테고리를 찾을 수 없습니다."));

    mockMvc.perform(MockMvcRequestBuilders.get("/categories/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("카테고리 생성 성공")
  void createCategory_Success() throws Exception {

    given(categoryService.saveCategory(any(CategoryDTO.class))).willReturn(1L);

    mockMvc.perform(MockMvcRequestBuilders.post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDTO)))
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("카테고리 생성 실패 - 잘못된 요청 데이터")
  void createCategory_Fail_BadRequest() throws Exception {

    CategoryDTO invalidCategoryDTO = new CategoryDTO(null, " ");

    mockMvc.perform(MockMvcRequestBuilders.post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidCategoryDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("카테고리 수정 성공")
  void updateCategory_Success() throws Exception {

    willDoNothing().given(categoryService).updateCategoryName(eq(1L), any(CategoryDTO.class));

    mockMvc.perform(MockMvcRequestBuilders.put("/categories/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDTO)))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("카테고리 수정 실패 - 존재하지 않는 카테고리")
  void updateCategory_Fail_NotFound() throws Exception {

    doThrow(new NotFoundException("카테고리를 찾을 수 없습니다.")).when(categoryService)
        .updateCategoryName(eq(1L), any(CategoryDTO.class));

    mockMvc.perform(MockMvcRequestBuilders.put("/categories/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDTO)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("카테고리 삭제 성공")
  void deleteCategory_Success() throws Exception {

    willDoNothing().given(categoryService).deleteCategory(1L);

    mockMvc.perform(MockMvcRequestBuilders.delete("/categories/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("카테고리 삭제 실패 - 존재하지 않는 카테고리")
  void deleteCategory_Fail_NotFound() throws Exception {

    doThrow(new NotFoundException("카테고리를 찾을 수 없습니다.")).when(categoryService).deleteCategory(1L);

    mockMvc.perform(MockMvcRequestBuilders.delete("/categories/1"))
        .andExpect(status().isNotFound());
  }
}
