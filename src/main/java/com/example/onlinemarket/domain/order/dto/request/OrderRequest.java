package com.example.onlinemarket.domain.order.dto.request;

import com.example.onlinemarket.domain.order.domain.Order;
import com.example.onlinemarket.domain.order.domain.OrderDetail;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private List<OrderDetailRequest> orderDetailRequest;

    public Order toEntity(Long userId, List<OrderDetail> orderDetails) {
        return Order.builder()
            .userId(userId)
            .orderDetails(orderDetails)
            .build();
    }
}
