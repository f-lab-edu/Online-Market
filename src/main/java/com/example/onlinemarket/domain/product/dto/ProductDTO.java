package com.example.onlinemarket.domain.product.dto;

import com.example.onlinemarket.domain.product.entity.Product;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private Long categoryId;
    private String name;
    private Long price;
    private Long quantity;
    private String description;
    private boolean isOutOfStock;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static ProductDto of(Product product) {
        return ProductDto.builder()
            .id(product.getId())
            .categoryId(product.getCategoryId())
            .name(product.getName())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .description(product.getDescription())
            .isOutOfStock(product.isOutOfStock())
            .createdDate(product.getCreatedDate())
            .updatedDate(product.getUpdatedDate())
            .build();
    }
}
