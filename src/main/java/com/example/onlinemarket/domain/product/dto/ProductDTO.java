package com.example.onlinemarket.domain.product.dto;

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
public class ProductDTO {

    private long id;

    @NotNull
    @NotEmpty
    private String name;

    @Positive
    @NotNull
    private Double price;

    @Min(1)
    private long quantity; // 상품 수량

    @NotNull
    @Size(min = 10, max = 200)
    private String description;
}
