package com.example.onlinemarket.domain.product.dto.response;

import com.example.onlinemarket.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private String name;
    private Long price;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
            .name(product.getName())
            .price(product.getPrice())
            .build();
    }
}
