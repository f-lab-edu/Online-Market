package com.example.onlinemarket.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.common.exception.UnauthorizedException;
import com.example.onlinemarket.domain.order.controller.OrderController;
import com.example.onlinemarket.domain.order.dto.OrderDTO;
import com.example.onlinemarket.domain.order.dto.OrderDetailDTO;
import com.example.onlinemarket.domain.order.eunm.OrderStatus;
import com.example.onlinemarket.domain.order.service.OrderService;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private OrderDTO orderDTO;
    private ProductDTO product1;
    private ProductDTO product2;

    @BeforeEach
    public void setUp() {
        product1 = new ProductDTO(101L, "First Product", 15000.0, 10, "First Product Description");
        product2 = new ProductDTO(102L, "Second Product", 30000.0, 5, "Second Product Description");

        List<OrderDetailDTO> orderDetailsList = new ArrayList<>();
        orderDetailsList.add(new OrderDetailDTO(0L, null, product1.getId(), 2, product1.getPrice()));
        orderDetailsList.add(new OrderDetailDTO(0L, null, product2.getId(), 1, product2.getPrice()));

        orderDTO = OrderDTO.builder().userId(1L).totalPrice(60000.0).orderTime(LocalDateTime.now())
            .status(OrderStatus.READY).orderDetails(orderDetailsList).build();
    }

    @Test
    @DisplayName("주문 생성 성공 시 201 Created 상태 코드 반환")
    public void testCreateOrderSuccess() throws Exception {
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(1L);

        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderDTO))).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("인증되지 않은 사용자가 주문 생성 실패하여 401 Unauthorized 상태 코드 반환")
    public void testCreateOrderAuthenticationFailure() throws Exception {
        when(orderService.createOrder(any(OrderDTO.class))).thenThrow(new UnauthorizedException("사용자 인증 실패"));

        mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
            .andExpect(status().isUnauthorized());
    }
}
