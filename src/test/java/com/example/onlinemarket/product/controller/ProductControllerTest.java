package com.example.onlinemarket.product.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.domain.product.controller.ProductController;
import com.example.onlinemarket.domain.product.dto.ProductDto;
import com.example.onlinemarket.domain.product.dto.request.ProductCreateRequest;
import com.example.onlinemarket.domain.product.dto.request.ProductUpdateRequest;
import com.example.onlinemarket.domain.product.dto.response.ProductDetailResponse;
import com.example.onlinemarket.domain.product.exception.NotFoundProductException;
import com.example.onlinemarket.domain.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private ProductDetailResponse productDetailResponse;
    private ProductCreateRequest productCreateRequest;
    private ProductDto productDto;

    @BeforeEach
    public void setUp() {
        productDetailResponse = ProductDetailResponse.builder()
            .name("name")
            .price(100L)
            .quantity(40L)
            .description("Test Description")
            .build();

        productCreateRequest = ProductCreateRequest.builder()
            .name("name")
            .price(100L)
            .quantity(40L)
            .description("Test Description")
            .build();

        productDto = ProductDto.builder()
            .name("name")
            .price(100L)
            .quantity(40L)
            .build();
    }

    @Test
    @DisplayName("모든 상품 조회 성공 시 200 Ok를 반환")
    public void testGetAllProductsSuccess() throws Exception {
        Long categoryId = 1L;
        int page = 1;
        int size = 10;
        List<ProductDto> allProducts = Collections.singletonList(productDto);

        when(productService.getProductsByCategory(categoryId, page, size)).thenReturn(allProducts);

        mockMvc.perform(get("/products/categories/{categoryId}", categoryId)
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(1)))
            .andExpect(jsonPath("$[0].name", is(productDto.getName())));
    }

    @Test
    @DisplayName("상품 이름으로 검색 성공 시 200 Ok 반환")
    public void testSearchProductsByNameSuccess() throws Exception {
        List<ProductDto> products = Collections.singletonList(productDto);

        when(productService.searchProductsByName("Test")).thenReturn(products);

        mockMvc.perform(get("/products/search")
                .param("productName", "Test")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(1)))
            .andExpect(jsonPath("$[0].name", is(productDto.getName())));
    }

    @Test
    @DisplayName("상품 이름으로 검색 실패 시 404 Not Found 반환 - 결과 없음")
    public void testSearchProductsByNameFailure() throws Exception {
        when(productService.searchProductsByName("nonexistent")).thenThrow(
            new NotFoundProductException("No products found"));

        mockMvc.perform(get("/products/search")
                .param("productName", "nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("모든 상품 조회 실패 시 404 Not Found 반환")
    public void testGetAllProductsNotFound() throws Exception {
        Long categoryId = 1L;
        int page = 1;
        int size = 10;

        when(productService.getProductsByCategory(categoryId, page, size)).thenThrow(
            new NotFoundProductException("Products not found"));

        mockMvc.perform(get("/products/categories/{categoryId}", categoryId)
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 ID로 특정 상품 조회 시 200 Ok 반환")
    public void testGetProductById() throws Exception {
        when(productService.getProductDetails(1L)).thenReturn(productDetailResponse);

        mockMvc.perform(get("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(productDetailResponse.getName())));
    }

    @Test
    @DisplayName("상품 ID로 특정 상품 조회 실패시 404 Not Found 상태 코드 반환")
    public void testFailGetProductById() throws Exception {
        when(productService.getProductDetails(1L)).thenThrow(
            new NotFoundProductException("Product not found"));

        mockMvc.perform(get("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 추가 성공 시 201 Created 상태 코드 반환")
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Long.class), refEq(productCreateRequest))).thenReturn(1L);

        mockMvc.perform(post("/products/categories/{categoryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCreateRequest)))
            .andExpect(status().isCreated());

        Mockito.verify(productService).createProduct(any(Long.class), refEq(productCreateRequest));
    }

    @Test
    @DisplayName("상품 추가 실패 시 400 Bad Request 상태 코드 반환")
    public void testFailCreateProduct() throws Exception {
        Mockito.doThrow(new NotFoundProductException("Invalid data")).when(productService)
            // 예외처리 클래스 변경 필요
            .createProduct(any(Long.class), refEq(productCreateRequest));

        mockMvc.perform(post("/products/categories/{categoryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productCreateRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 정보 업데이트 성공 시 200 OK 상태 코드 반환")
    public void testUpdateProduct() throws Exception {
        ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
            .name("updated name")
            .price(150L)
            .quantity(50L)
            .description("Updated Description")
            .build();

        mockMvc.perform(put("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productUpdateRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 정보 업데이트 실패 시 400 Bad Request 상태 코드 반환")
    public void testFailUpdateProduct() throws Exception {
        ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder().build();

        Mockito.doThrow(new NotFoundProductException("Invalid data")).when(productService)
            // 예외처리 클래스 변경 필요
            .updateProduct(any(Long.class), any(ProductUpdateRequest.class));

        mockMvc.perform(put("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productUpdateRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 정보 삭제 시 204 No Content 상태 코드를 반환한다.")
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 정보 삭제 실패 시 404 Not Found 상태 코드 반환")
    public void testFailDeleteProduct() throws Exception {
        Mockito.doThrow(new NotFoundProductException("Product not found")).when(productService)
            .deleteProduct(1L);

        mockMvc.perform(delete("/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
