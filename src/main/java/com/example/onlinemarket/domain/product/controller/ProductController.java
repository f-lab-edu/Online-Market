package com.example.onlinemarket.domain.product.controller;

import com.example.onlinemarket.common.response.ApiResponse;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import com.example.onlinemarket.domain.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequestMapping("/products")
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {

        List<ProductDTO> products = productService.getAllProducts();

        return ResponseEntity.ok(new ApiResponse<>(true, "All products retrieved", products));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable int id) {

        ProductDTO product = productService.getProductById(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "ProductId found", product));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> searchProductsByName(
        @NotBlank @RequestParam String productName) {

        List<ProductDTO> products = productService.searchProductsByName(productName);

        return ResponseEntity.ok(new ApiResponse<>(true, "Products found", products));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
        @RequestBody @Valid ProductDTO productDTO) {

        int productId = productService.createProduct(productDTO);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(productId).toUri();

        return ResponseEntity.created(location)
            .body(new ApiResponse<>(true, "Product created", null));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
        @RequestBody @Valid ProductDTO productDTO) {

        productService.updateProduct(productDTO);

        return ResponseEntity.ok(new ApiResponse<>(true, "Product updated", null));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable int id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted", null));
    }
}
