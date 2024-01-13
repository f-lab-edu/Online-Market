package com.example.onlinemarket.product.controller;

import static org.hamcrest.Matchers.hasSize;
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

import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.common.exception.ValidationException;
import com.example.onlinemarket.domain.product.controller.ProductController;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import com.example.onlinemarket.domain.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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

    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() {
        productDTO = ProductDTO.builder()
                .id(1)
                .name("geonhui")
                .price(1000.0)
                .description("Description")
                .build();
    }

    @Test
    @DisplayName("모든 상품 조회 성공 시 200 Ok를 반환")
    public void testGetAllProducts() throws Exception {
        List<ProductDTO> allProducts = Arrays.asList(productDTO);

        when(productService.getAllProducts()).thenReturn(allProducts);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(productDTO.getName())));
    }

    @Test
    @DisplayName("상품 이름으로 검색 성공 시 200 Ok 반환")
    public void testSearchProductsByNameSuccess() throws Exception {
        when(productService.searchProductsByName("Test")).thenReturn(
                Collections.singletonList(productDTO));

        mockMvc.perform(get("/products/search")
                        .param("productName", "Test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name", is(productDTO.getName())));
    }

    @Test
    @DisplayName("상품 이름으로 검색 실패 시 404 Not Found 반환 - 결과 없음")
    public void testSearchProductsByNameFailure() throws Exception {
        when(productService.searchProductsByName("nonexistent")).thenThrow(
                new NotFoundException("No products found"));

        mockMvc.perform(get("/products/search")
                        .param("productName", "nonexistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("모든 상품 조회 실패 시 404 Not Found 반환")
    public void testGetAllProductsNotFound() throws Exception {
        when(productService.getAllProducts()).thenThrow(
                new NotFoundException("Products not found"));

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 ID로 특정 상품 조회 시 200 Ok 반환")
    public void testGetProductById() throws Exception {
        when(productService.getProductById(1)).thenReturn(productDTO);

        mockMvc.perform(get("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(productDTO.getId())))
                .andExpect(jsonPath("$.data.name", is(productDTO.getName())));
    }


    @Test
    @DisplayName("상품 ID로 특정 상품 조회 실패시 404 Not Found 상태 코드 반환")
    public void testFailGetProductById() throws Exception {
        when(productService.getProductById(1)).thenThrow(
                new NotFoundException("Product not found"));

        mockMvc.perform(get("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 추가 성공 시 201 Created 상태 코드 반환")
    public void testCreateProduct() throws Exception {

        when(productService.createProduct(any())).thenReturn(productDTO.getId());

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated());

        Mockito.verify(productService).createProduct(refEq(productDTO));
    }

    @Test
    @DisplayName("상품 추가 실패 시 400 Bad Request 상태 코드 반환")
    public void testFailCreateProduct() throws Exception {
        Mockito.doThrow(new ValidationException()).when(productService)
                .createProduct(refEq(productDTO));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 정보 업데이트 성공 시 200 OK 상태 코드 반환")
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(any())).thenReturn(productDTO);

        mockMvc.perform(put("/products/{id}", productDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 정보 업데이트 실패 시 400 Bad Request 상태 코드 반환")
    public void testFailUpdateProduct() throws Exception {
        when(productService.updateProduct(any())).thenThrow(
                new ValidationException("Invalid data"));

        mockMvc.perform(put("/products/{id}", productDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 정보 삭제 시 200 OK 상태 코드를 반환한다.")
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 정보 삭제 성공 시 404 Not Found 상태 코드 반환")
    public void testFailDeleteProduct() throws Exception {
        Mockito.doThrow(new NotFoundException("Product not found")).when(productService)
                .deleteProduct(1);

        mockMvc.perform(delete("/products/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
