package com.example.onlinemarket.product.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.domain.category.dto.CategoryDTO;
import com.example.onlinemarket.domain.category.mapper.CategoryMapper;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import com.example.onlinemarket.domain.product.mapper.ProductMapper;
import com.example.onlinemarket.domain.product.service.ProductService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductDTO productDTO;
    private CategoryMapper categoryMapper;

    @BeforeEach
    public void setUp() {
        productDTO = new ProductDTO(1L, 1l, "categoryName", "Product Name", 100.0, 40, "Test Description");
        when(categoryMapper.findById(anyLong())).thenReturn(new CategoryDTO(1L, "TestCategory"));
    }

    @Test
    @DisplayName("상품 조회 성공 시 반환된 상품 목록 검증")
    public void testGetAllProductsSuccess() {
        // Arrange
        Long categoryId = 1L;
        int page = 1;
        int limit = 100;
        ProductDTO productDTO2 = ProductDTO.builder()
            .id(1L)
            .categoryId(categoryId)
            .name("Product Name")
            .price(100.0)
            .quantity(10)
            .description("Product Description")
            .build();

        List<ProductDTO> expectedProducts = Arrays.asList(productDTO2);

        when(productMapper.findAll(categoryId, 0, limit)).thenReturn(expectedProducts);

        // Act
        List<ProductDTO> actualProducts = productService.getAllProducts(categoryId, page, limit);

        // Assert
        assertEquals(expectedProducts, actualProducts);
        verify(productMapper, times(1)).findAll(categoryId, 0, limit);
    }


    @Test
    @DisplayName("상품 이름으로 검색 성공")
    public void testSearchProductsByNameSuccess() {
        List<ProductDTO> expectedProducts = Collections.singletonList(productDTO);
        when(productMapper.findByNameContaining("Test")).thenReturn(expectedProducts);

        List<ProductDTO> foundProducts = productService.searchProductsByName("Test");

        assertFalse(foundProducts.isEmpty(), "검색된 상품 목록이 비어있지 않아야 합니다.");
        assertEquals(expectedProducts.size(), foundProducts.size(), "검색된 상품 수가 일치해야 합니다.");
        assertEquals(expectedProducts.get(0).getId(), foundProducts.get(0).getId(),
            "검색된 상품의 ID가 일치해야 합니다.");
        assertEquals(expectedProducts.get(0).getName(), foundProducts.get(0).getName(),
            "검색된 상품의 이름이 일치해야 합니다.");
    }


    @Test
    @DisplayName("상품 이름으로 검색 실패 - 검색 결과 없음")
    public void testSearchProductsByNameFailure() {
        when(productMapper.findByNameContaining(anyString())).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class,
            () -> productService.searchProductsByName("nonexistent"));
    }

    @Test
    @DisplayName("상품 ID로 특정 상품 조회 성공")
    public void testGetProductByIdSuccess() {
        when(productMapper.findById(1)).thenReturn(productDTO);

        ProductDTO foundProduct = productService.getProductById(1);

        assertNotNull(foundProduct);
        assertEquals(productDTO, foundProduct);
    }

    @Test
    @DisplayName("상품 ID로 특정 상품 조회 실패 - 상품 없음")
    public void testGetProductByIdFailure() {
        when(productMapper.findById(1)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> productService.getProductById(1));
    }

    @Test
    @DisplayName("새 상품 추가 성공")
    public void testCreateProductSuccess() {
        productService.createProduct(productDTO);

        assertEquals(1L, productDTO.getId());
    }

    @Test
    @DisplayName("상품 업데이트 성공")
    public void testUpdateProductSuccess() {
        when(productMapper.findById(1)).thenReturn(productDTO);

        ProductDTO updatedProduct = productService.updateProduct(productDTO);

        assertNotNull(updatedProduct);
    }

    @Test
    @DisplayName("상품 업데이트 성공 시 상세한 검증")
    public void testUpdateProductSuccessDetailed() {
        when(productMapper.findById(1)).thenReturn(productDTO);
        ProductDTO updatedProduct = productService.updateProduct(productDTO);

        assertNotNull(updatedProduct);
        assertEquals(productDTO.getId(), updatedProduct.getId());
        assertEquals(productDTO.getName(), updatedProduct.getName());
        assertEquals(productDTO.getPrice(), updatedProduct.getPrice());
        assertEquals(productDTO.getDescription(), updatedProduct.getDescription());
    }

    @Test
    @DisplayName("상품 업데이트 실패 - 상품 ID 존재하지 않음")
    public void testUpdateProductFailureProductIdNotFound() {
        when(productMapper.findById(1)).thenReturn(null);

        assertThrows(NotFoundException.class,
            () -> productService.updateProduct(productDTO));
    }

    @Test
    @DisplayName("상품 삭제 성공")
    public void testDeleteProductSuccess() {
        when(productMapper.findById(1)).thenReturn(productDTO);
        assertDoesNotThrow(() -> productService.deleteProduct(1));
    }

    @Test
    @DisplayName("상품 삭제 실패 - 상품 ID 존재하지 않음")
    public void testDeleteProductFailure() {
        when(productMapper.findById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> productService.deleteProduct(1));
    }
}
