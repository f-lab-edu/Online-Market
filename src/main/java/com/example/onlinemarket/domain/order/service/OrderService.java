package com.example.onlinemarket.domain.order.service;

import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.common.exception.UnauthorizedException;
import com.example.onlinemarket.common.exception.ValidationException;
import com.example.onlinemarket.domain.order.domain.Order;
import com.example.onlinemarket.domain.order.domain.OrderDetail;
import com.example.onlinemarket.domain.order.dto.OrderDTO;
import com.example.onlinemarket.domain.order.dto.OrderDetailDTO;
import com.example.onlinemarket.domain.order.mapper.OrderDetailMapper;
import com.example.onlinemarket.domain.order.mapper.OrderMapper;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import com.example.onlinemarket.domain.product.mapper.ProductMapper;
import com.example.onlinemarket.domain.user.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductMapper productMapper;

    @Qualifier("sessionLoginService")
    private final LoginService loginService;

    @Transactional
    public Long createOrder(OrderDTO orderDTO) throws NotFoundException {
        validateOrderData(orderDTO);

        Order order = orderDTO.toEntity();
        double totalPrice = 0.0;

        for (OrderDetailDTO orderDetail : orderDTO.getOrderDetails()) {
            ProductDTO product = productMapper.findById(orderDetail.getProductId());
            if (product == null) {
                throw new NotFoundException("상품 ID가 존재하지 않습니다");
            }

            long newQuantity = product.getQuantity() - orderDetail.getQuantity();
            if (newQuantity <= 0) {
                throw new ValidationException("상품 수량이 모두 소진 되었습니다.");
            }
            productMapper.updateQuantity(product.getId(), newQuantity);

            double detailTotalPrice = orderDetail.getQuantity() * product.getPrice();
            totalPrice += detailTotalPrice;

            OrderDetail newOrderDetail = orderDetail.toEntity(order.getId(), product);
            orderDetailMapper.insert(newOrderDetail);
        }

        order.setTotalPrice(totalPrice);
        orderMapper.insert(order);

        return order.getId();
    }

    public void validateOrderData(OrderDTO orderDTO) throws UnauthorizedException {
        long loggedInUserId;
        try {
            loggedInUserId = loginService.getLoginUserId();
        } catch (UnauthorizedException e) {
            throw new RuntimeException("사용자가 로그인 상태가 아닙니다. 로그인 페이지로 이동합니다.");
        }

        if (orderDTO.getUserId() != loggedInUserId) {
            throw new RuntimeException("주문자 정보와 로그인한 사용자 정보가 일치하지 않습니다.");
        }
    }
}
