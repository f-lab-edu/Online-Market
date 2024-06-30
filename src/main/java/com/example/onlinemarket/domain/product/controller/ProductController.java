package com.example.onlinemarket.domain.product.controller;

import com.example.onlinemarket.domain.product.dto.request.ProductCreateRequest;
import com.example.onlinemarket.domain.product.dto.request.ProductUpdateRequest;
import com.example.onlinemarket.domain.product.dto.response.ProductDetailResponse;
import com.example.onlinemarket.domain.product.dto.response.ProductListResponse;
import com.example.onlinemarket.domain.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/products")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/categories/{categoryId}")
    public ResponseEntity<Long> createProduct(
        @PathVariable Long categoryId,
        @RequestBody @Valid ProductCreateRequest request) {

        long id = productService.createProduct(categoryId, request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ProductListResponse> getProductsByCategory(
        @PathVariable Long categoryId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "100") int limit) {

        ProductListResponse products = productService.getProductsByCategory(categoryId, page, limit);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductDetails(@PathVariable int id) {
        ProductDetailResponse productDetailResponse = productService.getProductDetails(id);
        return new ResponseEntity<>(productDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ProductListResponse> searchProductsByName(
        @NotBlank @RequestParam String name) {
        ProductListResponse searchProducts = productService.searchProductsByName(name);
        return new ResponseEntity<>(searchProducts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductUpdateRequest request) {
        productService.updateProduct(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
