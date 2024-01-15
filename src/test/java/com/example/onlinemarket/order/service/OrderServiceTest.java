package com.example.onlinemarket.order.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.common.exception.UnauthorizedException;
import com.example.onlinemarket.common.exception.ValidationException;
import com.example.onlinemarket.domain.order.domain.Order;
import com.example.onlinemarket.domain.order.domain.OrderDetail;
import com.example.onlinemarket.domain.order.dto.OrderDTO;
import com.example.onlinemarket.domain.order.dto.OrderDetailDTO;
import com.example.onlinemarket.domain.order.eunm.OrderStatus;
import com.example.onlinemarket.domain.order.mapper.OrderDetailMapper;
import com.example.onlinemarket.domain.order.mapper.OrderMapper;
import com.example.onlinemarket.domain.order.service.OrderService;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import com.example.onlinemarket.domain.product.mapper.ProductMapper;
import com.example.onlinemarket.domain.user.service.LoginService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderDetailMapper orderDetailMapper;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private LoginService loginService;

    private OrderService orderService;
    private List<OrderDetailDTO> orderDetailsList;
    private ProductDTO product1;
    private ProductDTO product2;
    private ProductDTO product3;


    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderMapper, orderDetailMapper, productMapper,
            loginService);

        product1 = new ProductDTO(1L, "Product", 1000.0, 10, "Description");
        product2 = new ProductDTO(2L, "Another Product", 2000.0, 20, "Another Description");
        product3 = new ProductDTO(3L, "Noquantity Product", 2000.0, 0, " Description");
    }

    @Test
    @DisplayName("주문 생성 성공 테스트")
    public void testCreateOrderSuccess() throws Exception {
        when(loginService.getLoginUserId()).thenReturn(1L);
        when(productMapper.findById(1L)).thenReturn(product1);
        when(productMapper.findById(2L)).thenReturn(product2);
        when(orderMapper.insert(any(Order.class))).thenReturn(1L);
        when(orderDetailMapper.insert(any(OrderDetail.class))).thenReturn(1L);

        OrderDTO orderDTO = createValidOrderDTO();
        Long orderId = orderService.createOrder(orderDTO);

        assertNotNull(orderId);
    }

    @Test
    @DisplayName("주문 생성 실패 테스트")
    public void testCreateOrderFailure() {
        when(loginService.getLoginUserId()).thenReturn(1L);
        when(productMapper.findById(1L)).thenReturn(null);

        OrderDTO invalidOrderDTO = createValidOrderDTO();

        assertThrows(NotFoundException.class, () -> orderService.createOrder(invalidOrderDTO));
    }

    @Test
    @DisplayName("상품 ID 존재 확인 테스트")
    public void testCheckProductExistence() {
        when(productMapper.findById(1L)).thenReturn(product1);

        ProductDTO product = productMapper.findById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
    }

    @Test
    @DisplayName("상품 ID가 존재하지 않을 때 실패 테스트")
    public void testCreateOrderWithInvalidProductId() {
        when(loginService.getLoginUserId()).thenReturn(1L);
        when(productMapper.findById(1L)).thenReturn(null);

        OrderDTO orderDTO = createValidOrderDTO();

        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    @DisplayName("재고가 있어 주문 성공 테스트")
    public void testOrderSuccessWithSufficientStock() {
        when(loginService.getLoginUserId()).thenReturn(1L);
        when(productMapper.findById(1L)).thenReturn(product1);
        when(productMapper.findById(2L)).thenReturn(product2);

        OrderDTO validOrderDTO = createValidOrderDTO();
        Long orderId = orderService.createOrder(validOrderDTO);

        assertNotNull(orderId);
    }

    @Test
    @DisplayName("재고 부족으로 주문 실패 테스트")
    public void testCreateOrderWithInsufficientStock() {
        when(loginService.getLoginUserId()).thenReturn(1L);
        when(productMapper.findById(1L)).thenReturn(product3);

        OrderDTO orderDTO = createValidOrderDTO();

        assertThrows(ValidationException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    @DisplayName("사용자 인증 성공 테스트")
    public void testUserAuthenticationSuccess() {
        when(loginService.getLoginUserId()).thenReturn(1L);

        OrderDTO validOrderDTO = createValidOrderDTO();
        validOrderDTO.setUserId(1L);

        assertDoesNotThrow(() -> orderService.validateOrderData(validOrderDTO));
    }

    @Test
    @DisplayName("사용자 인증 실패 테스트")
    public void testCreateOrderWithUnauthenticatedUser() {
        when(loginService.getLoginUserId()).thenThrow(new UnauthorizedException("인증 실패"));

        OrderDTO orderDTO = createValidOrderDTO();

        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderDTO));
    }

    private OrderDTO createValidOrderDTO() {
        List<OrderDetailDTO> orderDetails = Arrays.asList(
            new OrderDetailDTO(0L, 1L, 1L, 1, 1000.0),
            new OrderDetailDTO(0L, 1L, 2L, 2, 2000.0));

        return new OrderDTO(1L, 1L, 3000.0, LocalDateTime.now(), OrderStatus.READY, orderDetails);
    }
}
