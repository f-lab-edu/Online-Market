package com.example.onlinemarket.domain.order.domain;

import com.example.onlinemarket.domain.order.dto.OrderDetailDTO;
import com.example.onlinemarket.domain.order.eunm.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Order {

    private Long id;
    private Long userId;
    private Double totalPrice;
    private LocalDateTime orderTime;
    private OrderStatus status;
    private List<OrderDetailDTO> orderDetails;

    @Builder
    public Order(Long id, Long userId, Double totalPrice, LocalDateTime orderTime, OrderStatus status, List<OrderDetailDTO> orderDetails) {

        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
        this.status = status;
        this.orderDetails = orderDetails;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
