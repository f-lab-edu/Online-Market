package com.example.onlinemarket.domain.order.dto.request;


import com.example.onlinemarket.domain.order.domain.OrderDetail;
import com.example.onlinemarket.domain.product.entity.Product;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {

    private Long productId;

    @Min(1)
    private Long productQuantity;

    public OrderDetail toEntity(Product product) {
        return OrderDetail.builder()
            .productId(productId)
            .productName(product.getName())
            .productPrice(product.getPrice())
            .productQuantity(productQuantity)
            .build();
    }
}
