package com.example.onlinemarket.domain.product.dto.request;

import com.example.onlinemarket.domain.product.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    private Long id;

    private Long categoryId;

    @NotNull
    private String name;

    @Positive
    @NotNull
    private Long price;

    @Min(1)
    private Long quantity;

    @NotNull
    @Size(min = 10, max = 200)
    private String description;

    private boolean isOutOfStock;

    private LocalDateTime updatedDate;

    public Product toEntity(Long productId) {
        return Product.builder()
            .id(productId)
            .categoryId(categoryId)
            .name(name)
            .price(price)
            .quantity(quantity)
            .description(description)
            .isOutOfStock(isOutOfStock)
            .updatedDate(LocalDateTime.now())
            .build();
    }
}
