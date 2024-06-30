package com.example.onlinemarket.order.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.domain.order.domain.Order;
import com.example.onlinemarket.domain.order.domain.OrderDetail;
import com.example.onlinemarket.domain.order.dto.request.OrderDetailRequest;
import com.example.onlinemarket.domain.order.dto.request.OrderRequest;
import com.example.onlinemarket.domain.order.mapper.OrderDetailMapper;
import com.example.onlinemarket.domain.order.mapper.OrderMapper;
import com.example.onlinemarket.domain.order.service.OrderService;
import com.example.onlinemarket.domain.product.entity.Product;
import com.example.onlinemarket.domain.product.exception.NotFoundProductException;
import com.example.onlinemarket.domain.product.mapper.ProductMapper;
import com.example.onlinemarket.domain.product.service.ProductService;
import com.example.onlinemarket.domain.user.exception.NotFoundUserIdException;
import com.example.onlinemarket.domain.user.service.UserService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderDetailMapper orderDetailMapper;

    @Mock
    private ProductService productService;
    @Mock
    private ProductMapper productMapper;

    @Mock
    private UserService userService;


    private OrderService orderService;
    private Product product1;
    private Product product2;
    private Product noQuantityProduct;
    private OrderRequest createValidOrderRequest;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderMapper, orderDetailMapper, userService, productService);

        product1 = Product.builder()
            .id(1L)
            .categoryId(1L)
            .price(100L)
            .quantity(40L)
            .description("Test Description")
            .build();

        product2 = Product.builder()
            .id(2L)
            .categoryId(2L)
            .name("Product Name")
            .price(1000L)
            .quantity(10L)
            .description("Product Description")
            .build();

        noQuantityProduct = Product.builder()
            .id(3L)
            .categoryId(2L)
            .name("No Quantity Product Name")
            .price(1000L)
            .quantity(0L)
            .description("Product Description")
            .build();

        OrderDetailRequest detail1 = OrderDetailRequest.builder()
            .productId(product1.getId())
            .productQuantity(1L)
            .build();

        OrderDetailRequest detail2 = OrderDetailRequest.builder()
            .productId(product2.getId())
            .productQuantity(2L)
            .build();

        createValidOrderRequest = OrderRequest.builder()
            .orderDetailRequest(Arrays.asList(detail1, detail2))
            .build();
    }

    @Test
    @DisplayName("주문 생성 성공 테스트")
    public void testCreateOrderSuccess() throws Exception {
        when(userService.isExists(1L)).thenReturn(true);
        when(productMapper.findById(1L)).thenReturn(Optional.of(product1));
        when(productMapper.findById(2L)).thenReturn(Optional.of(product2));
        when(orderMapper.insert(any(Order.class))).thenReturn(1L);
        when(orderDetailMapper.insert(any(OrderDetail.class))).thenReturn(1L);

        Long orderId = orderService.createOrder(1L, createValidOrderRequest);

        assertNotNull(orderId);
    }

    @Test
    @DisplayName("주문 생성 실패 테스트")
    public void testCreateOrderFailure() {
        when(userService.isExists(1L)).thenReturn(true);
        when(productMapper.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundProductException.class, () -> orderService.createOrder(1L, createValidOrderRequest));
    }

    @Test
    @DisplayName("상품 ID가 존재하지 않을 때 실패 테스트")
    public void testCreateOrderWithInvalidProductId() {
        when(userService.isExists(1L)).thenReturn(true);
        when(productMapper.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundProductException.class, () -> orderService.createOrder(1L, createValidOrderRequest));
    }

    @Test
    @DisplayName("재고가 있어 주문 성공 테스트")
    public void testOrderSuccessWithSufficientStock() {
        when(userService.isExists(1L)).thenReturn(true);
        when(productMapper.findById(1L)).thenReturn(Optional.of(product1));
        when(productMapper.findById(2L)).thenReturn(Optional.of(product2));
        when(orderMapper.insert(any(Order.class))).thenReturn(1L);
        when(orderDetailMapper.insert(any(OrderDetail.class))).thenReturn(1L);

        Long orderId = orderService.createOrder(1L, createValidOrderRequest);

        assertNotNull(orderId);
    }

    @Test
    @DisplayName("재고 부족으로 주문 실패 테스트")
    public void testCreateOrderWithInsufficientStock() {
        when(userService.isExists(1L)).thenReturn(true);
        when(productMapper.findById(1L)).thenReturn(Optional.of(noQuantityProduct));

        assertThrows(RuntimeException.class, () -> orderService.createOrder(1L, createValidOrderRequest));
    }

    @Test
    @DisplayName("사용자 인증 성공 테스트")
    public void testUserAuthenticationSuccess() {
        when(userService.isExists(1L)).thenReturn(true);

        assertDoesNotThrow(() -> orderService.createOrder(1L, createValidOrderRequest));
    }

    @Test
    @DisplayName("사용자 인증 실패 테스트")
    public void testCreateOrderWithUnauthenticatedUser() {
        when(userService.isExists(1L)).thenReturn(false);

        assertThrows(NotFoundUserIdException.class, () -> orderService.createOrder(1L, createValidOrderRequest));
    }
}
