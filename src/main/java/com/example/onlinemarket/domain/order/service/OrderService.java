package com.example.onlinemarket.domain.order.service;

import com.example.onlinemarket.domain.order.domain.Order;
import com.example.onlinemarket.domain.order.domain.OrderDetail;
import com.example.onlinemarket.domain.order.dto.request.OrderDetailRequest;
import com.example.onlinemarket.domain.order.dto.request.OrderRequest;
import com.example.onlinemarket.domain.order.mapper.OrderDetailMapper;
import com.example.onlinemarket.domain.order.mapper.OrderMapper;
import com.example.onlinemarket.domain.product.entity.Product;
import com.example.onlinemarket.domain.product.service.ProductService;
import com.example.onlinemarket.domain.user.exception.NotFoundUserIdException;
import com.example.onlinemarket.domain.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    public Long createOrder(Long userId, OrderRequest request) {
        checkUserExists(userId);

        List<OrderDetail> orderDetails = request.getOrderDetailRequest().stream()
            .map(this::createOrderProduct)
            .collect(Collectors.toList());

        Order newOrder = new Order(userId, orderDetails);
        orderMapper.insert(newOrder);

        orderDetails.forEach(orderDetail -> {
            orderDetail.setOrderId(newOrder.getId());
            orderDetailMapper.insert(orderDetail);
        });

        return newOrder.getId();
    }

    private void checkUserExists(Long userId) {
        if (!userService.isExists(userId)) {
            throw new NotFoundUserIdException("사용자 아이디가 존재하지 않습니다.");
        }
    }

    private OrderDetail createOrderProduct(OrderDetailRequest request) {
        Product product = productService.checkProductExists(request.getProductId());
        productService.decreaseStock(product, request.getProductQuantity());
        return request.toEntity(product);
    }
}
