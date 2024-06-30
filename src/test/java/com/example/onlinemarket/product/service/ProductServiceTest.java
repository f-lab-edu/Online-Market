package com.example.onlinemarket.product.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.example.onlinemarket.domain.category.entity.Category;
import com.example.onlinemarket.domain.category.mapper.CategoryMapper;
import com.example.onlinemarket.domain.product.dto.ProductDto;
import com.example.onlinemarket.domain.product.dto.request.ProductCreateRequest;
import com.example.onlinemarket.domain.product.dto.request.ProductUpdateRequest;
import com.example.onlinemarket.domain.product.dto.response.ProductDetailResponse;
import com.example.onlinemarket.domain.product.entity.Product;
import com.example.onlinemarket.domain.product.exception.NotFoundProductException;
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

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private ProductService productService;

    private ProductDetailResponse productDetailResponse;
    private Category category;
    private Product product;
    private ProductCreateRequest productCreateRequest;
    private ProductUpdateRequest productUpdateRequest;

    @BeforeEach
    public void setUp() {

        product = Product.builder()
            .id(1L)
            .categoryId(1L)
            .name("Test Product")
            .price(100L)
            .quantity(40L)
            .description("Test Description")
            .build();

        category = Category.builder()
            .id(1L)
            .name("TestCategory")
            .build();

        productDetailResponse = ProductDetailResponse.builder()
            .name("Test Product")
            .price(100L)
            .quantity(40L)
            .description("Test Description")
            .build();

        productCreateRequest = ProductCreateRequest.builder()
            .name("Test Product")
            .price(100L)
            .quantity(40L)
            .description("Test Description")
            .build();

        productUpdateRequest = ProductUpdateRequest.builder()
            .name("Updated Product")
            .price(150L)
            .quantity(30L)
            .description("Updated Description")
            .build();

        when(categoryMapper.findById(anyLong())).thenReturn(null);
    }

    @Test
    @DisplayName("상품 조회 성공 시 반환된 상품 목록 검증")
    public void testGetAllProductsSuccess() {
        Long categoryId = 1L;
        int page = 1;
        int limit = 100;
        Product product2 = Product.builder()
            .id(1L)
            .categoryId(categoryId)
            .name("Product Name")
            .price(100L)
            .quantity(10L)
            .description("Product Description")
            .build();

        List<Product> expectedProducts = Arrays.asList(product2);

        when(productMapper.selectProductsByCategory(categoryId, 0, limit)).thenReturn(expectedProducts);

        List<ProductDto> actualProducts = productService.getProductsByCategory(categoryId, page, limit);

        assertEquals(expectedProducts.size(), actualProducts.size());
        verify(productMapper, times(1)).selectProductsByCategory(categoryId, 0, limit);
    }

    @Test
    @DisplayName("상품 이름으로 검색 성공")
    public void testSearchProductsByNameSuccess() {
        List<Product> expectedProducts = Collections.singletonList(product);
        when(productMapper.findByNameContaining("Test")).thenReturn(expectedProducts);

        List<ProductDto> foundProducts = productService.searchProductsByName("Test");

        assertFalse(foundProducts.isEmpty(), "검색된 상품 목록이 비어있지 않아야 합니다.");
        assertEquals(expectedProducts.size(), foundProducts.size(), "검색된 상품 수가 일치해야 합니다.");
        assertEquals(expectedProducts.get(0).getId(), foundProducts.get(0).getId(), "검색된 상품의 ID가 일치해야 합니다.");
        assertEquals(expectedProducts.get(0).getName(), foundProducts.get(0).getName(), "검색된 상품의 이름이 일치해야 합니다.");
    }

    @Test
    @DisplayName("상품 이름으로 검색 실패 - 검색 결과 없음")
    public void testSearchProductsByNameFailure() {
        when(productMapper.findByNameContaining(anyString())).thenReturn(Collections.emptyList());
        assertThrows(NotFoundProductException.class, () -> productService.searchProductsByName("nonexistent"));
    }

    @Test
    @DisplayName("상품 ID로 특정 상품 조회 성공")
    public void testGetProductByIdSuccess() {
        when(productMapper.findById(1L)).thenReturn(java.util.Optional.ofNullable(product));

        ProductDetailResponse foundProduct = productService.getProductDetails(1L);

        assertNotNull(foundProduct);
        assertEquals(productDetailResponse.getName(), foundProduct.getName());
    }

    @Test
    @DisplayName("상품 ID로 특정 상품 조회 실패 - 상품 없음")
    public void testGetProductByIdFailure() {
        when(productMapper.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundProductException.class, () -> productService.getProductDetails(1L));
    }

    @Test
    @DisplayName("새 상품 추가 성공")
    public void testCreateProductSuccess() {
        assertDoesNotThrow(() -> productService.createProduct(1L, productCreateRequest));
        verify(productMapper, times(1)).insert(any(Product.class));
    }

    @Test
    @DisplayName("상품 업데이트 성공")
    public void testUpdateProductSuccess() {
        when(productMapper.findById(1L)).thenReturn(java.util.Optional.ofNullable(product));

        assertDoesNotThrow(() -> productService.updateProduct(1L, productUpdateRequest));
    }

    @Test
    @DisplayName("상품 업데이트 성공 시 상세한 검증")
    public void testUpdateProductSuccessDetailed() {
        when(productMapper.findById(1L)).thenReturn(java.util.Optional.ofNullable(product));

        assertDoesNotThrow(() -> productService.updateProduct(1L, productUpdateRequest));

        assertEquals(productUpdateRequest.getName(), product.getName());
        assertEquals(productUpdateRequest.getPrice(), product.getPrice());
        assertEquals(productUpdateRequest.getQuantity(), product.getQuantity());
        assertEquals(productUpdateRequest.getDescription(), product.getDescription());
    }

    @Test
    @DisplayName("상품 업데이트 실패 - 상품 ID 존재하지 않음")
    public void testUpdateProductFailureProductIdNotFound() {
        when(productMapper.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundProductException.class, () -> productService.updateProduct(1L, productUpdateRequest));
    }

    @Test
    @DisplayName("상품 삭제 성공")
    public void testDeleteProductSuccess() {
        when(productMapper.findById(1L)).thenReturn(java.util.Optional.ofNullable(product));
        assertDoesNotThrow(() -> productService.deleteProduct(1L));
    }

    @Test
    @DisplayName("상품 삭제 실패 - 상품 ID 존재하지 않음")
    public void testDeleteProductFailure() {
        when(productMapper.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(NotFoundProductException.class, () -> productService.deleteProduct(1L));
    }
}
