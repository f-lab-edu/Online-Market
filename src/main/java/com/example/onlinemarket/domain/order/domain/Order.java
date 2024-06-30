package com.example.onlinemarket.domain.order.domain;

import com.example.onlinemarket.domain.order.eunm.OrderStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    private Long id;
    private Long userId;
    private String orderNumber;
    private OrderStatus status;
    private Long totalPrice;
    private LocalDateTime orderTime;
    private List<OrderDetail> orderDetails;

    public Order(Long userId, List<OrderDetail> orderDetails) {
        this.userId = userId;
        this.orderNumber = generateOrderNumber();
        this.status = OrderStatus.READY;
        this.totalPrice = calculateTotalPrice(orderDetails);
        this.orderTime = LocalDateTime.now();
    }

    private String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String datetime = LocalDateTime.now().format(formatter);
        int randomNumber = new Random().nextInt(9000) + 1000;
        return datetime + userId + randomNumber;
    }

    private Long calculateTotalPrice(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
            .mapToLong(detail -> detail.getProductPrice() * detail.getProductQuantity())
            .sum();
    }

}
