package com.example.onlinemarket.domain.order.dto;

import com.example.onlinemarket.domain.order.domain.OrderDetail;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {

    private long id;

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;

    @NotNull
    private Double price;

    public OrderDetail toEntity(Long orderId, ProductDTO product) {

        return OrderDetail.builder()
            .orderId(orderId)
            .productId(productId)
            .productName(product.getName())
            .productPrice(product.getPrice())
            .productQuantity(product.getQuantity())
            .build();
    }
}
