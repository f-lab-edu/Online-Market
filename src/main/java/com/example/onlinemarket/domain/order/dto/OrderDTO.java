package com.example.onlinemarket.domain.order.dto;

import com.example.onlinemarket.domain.order.domain.Order;
import com.example.onlinemarket.domain.order.eunm.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private long id;

    @NotNull
    private Long userId;

    @NotNull
    @PositiveOrZero
    private Double totalPrice;

    @NotNull
    private LocalDateTime orderTime;

    @NotNull
    private OrderStatus status;

    @NotNull
    private List<OrderDetailDTO> orderDetails;

    public Order toEntity() {

        return Order.builder()
            .id(id)
            .userId(userId)
            .totalPrice(totalPrice)
            .orderTime(orderTime)
            .status(status)
            .build();
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
