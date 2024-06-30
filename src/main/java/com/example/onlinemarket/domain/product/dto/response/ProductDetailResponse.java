package com.example.onlinemarket.domain.product.dto.response;

import com.example.onlinemarket.domain.product.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {

    @NotNull
    @NotEmpty
    private String name;

    @Positive
    @NotNull
    private Long price;

    // 할인된 가격

    @Min(1)
    private Long quantity;

    @NotNull
    @Size(min = 10, max = 200)
    private String description;

    private String categoryName;

    public static ProductDetailResponse of(Product product, String categoryName) {
        return ProductDetailResponse.builder()
            .name(product.getName())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .description(product.getDescription())
            .categoryName(categoryName)
            .build();
    }
}
