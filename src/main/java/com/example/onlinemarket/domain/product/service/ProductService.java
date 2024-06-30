package com.example.onlinemarket.domain.product.service;

import com.example.onlinemarket.common.Redis.RedisService;
import com.example.onlinemarket.domain.category.mapper.CategoryMapper;
import com.example.onlinemarket.domain.category.service.CategoryService;
import com.example.onlinemarket.domain.product.dto.request.ProductCreateRequest;
import com.example.onlinemarket.domain.product.dto.request.ProductUpdateRequest;
import com.example.onlinemarket.domain.product.dto.response.ProductDetailResponse;
import com.example.onlinemarket.domain.product.dto.response.ProductListResponse;
import com.example.onlinemarket.domain.product.dto.response.ProductResponse;
import com.example.onlinemarket.domain.product.entity.Product;
import com.example.onlinemarket.domain.product.exception.NotFoundProductException;
import com.example.onlinemarket.domain.product.mapper.ProductMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final RedisService redisService;

    public long createProduct(long categoryId, ProductCreateRequest request) {
        categoryService.findById(categoryId);
        Product product = request.toEntity(categoryId);
        productMapper.insert(product);

        return product.getId();
    }

    public ProductListResponse getProductsByCategory(Long categoryId, int page, int limit) {
        String key = "category:" + categoryId + ":page:" + page + ":limit:" + limit;
        List<Product> products = (List<Product>) redisService.getValues(key);

        if (products == null) {
            int offset = (page - 1) * limit;
            products = productMapper.selectProductsByCategory(categoryId, offset, limit);
            redisService.setValues(key, products, 10, TimeUnit.MINUTES);
        }

        List<ProductResponse> productResponses = products.stream()
            .map(ProductResponse::of)
            .toList();

        return new ProductListResponse(productResponses);
    }

    public ProductListResponse searchProductsByName(String name) {
        List<Product> searchedProducts = productMapper.findByNameContaining(name);
        if (searchedProducts.isEmpty()) {
            throw new NotFoundProductException("해당 상품을 찾을 수 없습니다.");
        }

        List<ProductResponse> productResponses = searchedProducts.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());

        return new ProductListResponse(productResponses);
    }

    public ProductDetailResponse getProductDetails(long id) {
        return productMapper.findById(id)
            .map(product -> {
                String categoryName = categoryMapper.selectCategoryName(product.getCategoryId());

                return ProductDetailResponse.of(product, categoryName);
            })
            .orElseThrow(() -> new NotFoundProductException("해당 상품을 찾을 수 없습니다."));
    }

    public void updateProduct(Long id, ProductUpdateRequest updateRequest) {
        checkProductExists(id);
        Product product = updateRequest.toEntity(id);
        productMapper.update(product);
    }

    public void deleteProduct(Long id) {
        checkProductExists(id);
        productMapper.deleteById(id);
    }

    public Product checkProductExists(Long id) {
        return productMapper.findById(id)
            .orElseThrow(() -> new NotFoundProductException("해당 상품을 찾을 수 없습니다."));
    }

    public void decreaseStock(Product product, Long productQuantity) {
        product.decreaseStock(productQuantity);
        productMapper.update(product);
    }
}
