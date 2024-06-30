package com.example.onlinemarket.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.domain.order.controller.OrderController;
import com.example.onlinemarket.domain.order.dto.request.OrderDetailRequest;
import com.example.onlinemarket.domain.order.dto.request.OrderRequest;
import com.example.onlinemarket.domain.order.service.OrderService;
import com.example.onlinemarket.domain.user.exception.UnauthorizedUserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private OrderRequest orderRequest;

    @BeforeEach
    public void setUp() {
        OrderDetailRequest detail1 = OrderDetailRequest.builder()
            .productId(1L)
            .productQuantity(2L)
            .build();

        OrderDetailRequest detail2 = OrderDetailRequest.builder()
            .productId(2L)
            .productQuantity(1L)
            .build();

        orderRequest = OrderRequest.builder()
            .orderDetailRequest(Arrays.asList(detail1, detail2))
            .build();
    }

    @Test
    @DisplayName("주문 생성 성공 시 201 Created 상태 코드 반환")
    public void testCreateOrderSuccess() throws Exception {
        when(orderService.createOrder(any(Long.class), any(OrderRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/orders")
                .sessionAttr("LOGGED_IN_USER", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("인증되지 않은 사용자가 주문 생성 실패하여 401 Unauthorized 상태 코드 반환")
    public void testCreateOrderAuthenticationFailure() throws Exception {
        when(orderService.createOrder(any(Long.class), any(OrderRequest.class))).thenThrow(new UnauthorizedUserException("사용자 인증 실패"));

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
            .andExpect(status().isUnauthorized());
    }
}
