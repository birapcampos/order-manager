package br.com.campos.order.service;

import br.com.campos.order.controller.dto.request.OrderItemRequest;
import br.com.campos.order.controller.dto.request.OrderRequest;
import br.com.campos.order.controller.dto.response.OrderResponse;
import br.com.campos.order.exceptions.OrderNotFoundException;
import br.com.campos.order.exceptions.ProductDuplicatedException;
import br.com.campos.order.repository.OrderRepository;
import br.com.campos.order.repository.ProductRepository;
import br.com.campos.order.repository.entity.OrderEntity;
import br.com.campos.order.repository.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private ProductEntity product;
    private OrderRequest orderRequest;
    private OrderItemRequest itemRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        product = ProductEntity.builder()
                .id("prod-001")
                .name("Produto Teste")
                .price(100.0)
                .build();

        itemRequest = OrderItemRequest.builder()
                .productId("prod-001")
                .productPrice(100.0)
                .quantity(2)
                .build();

        orderRequest = OrderRequest.builder()
                .customerId(123L)
                .orderDate(LocalDate.now())
                .items(Arrays.asList(itemRequest))
                .build();
    }

    @Test
    public void testCreateOrder_Success() {
        // Arrange
        when(productRepository.findAllById(Arrays.asList("prod-001"))).thenReturn(Arrays.asList(product));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(OrderEntity.builder()
                .id("order-001")
                .customerId(123L)
                .orderDate(orderRequest.getOrderDate())
                .status("COMPLETE")
                .orderAmount(200.0)
                .items(Arrays.asList(OrderEntity.OrderItem.builder()
                        .productId("prod-001")
                        .productPrice(100.0)
                        .quantity(2)
                        .build()))
                .build());

        // Act
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(orderResponse);
        assertEquals("order-001", orderResponse.getOrderId());
        assertEquals(200.0, orderResponse.getOrderAmount());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    public void testCreateOrder_ProductNotFound() {
        // Arrange
        when(productRepository.findAllById(Arrays.asList("prod-001"))).thenReturn(Arrays.asList());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(orderRequest);
        });
        assertEquals("Os seguintes produtos não existem: [prod-001]", exception.getMessage());
    }

    @Test
    public void testCreateOrder_DuplicateItems() {
        // Arrange
        OrderItemRequest duplicateItem = OrderItemRequest.builder()
                .productId("prod-001")
                .productPrice(100.0)
                .quantity(1)
                .build();
        orderRequest.setItems(Arrays.asList(itemRequest, duplicateItem));

        // Act & Assert
        ProductDuplicatedException exception = assertThrows(ProductDuplicatedException.class, () -> {
            orderService.createOrder(orderRequest);
        });
        assertEquals("Há produtos repetidos na lista.", exception.getMessage());
    }

    @Test
    public void testGetOrdersByDateAndStatus_Success() {
        // Arrange
        when(orderRepository.findByOrderDateAndStatus(orderRequest.getOrderDate(), "COMPLETE"))
                .thenReturn(Arrays.asList(OrderEntity.builder()
                        .id("order-001")
                        .customerId(123L)
                        .orderDate(orderRequest.getOrderDate())
                        .status("COMPLETE")
                        .orderAmount(200.0)
                        .items(Arrays.asList(OrderEntity.OrderItem.builder()
                                .productId("prod-001")
                                .productPrice(100.0)
                                .quantity(2)
                                .build()))
                        .build()));

        // Act
        List<OrderResponse> orders = orderService.getOrdersByDateAndStatus(
                orderRequest.getOrderDate(), "COMPLETE");

        // Assert
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals("order-001", orders.get(0).getOrderId());
    }

    @Test
    public void testGetOrdersByDateAndStatus_NotFound() {
        // Arrange
        when(orderRepository.findByOrderDateAndStatus(orderRequest.getOrderDate(), "COMPLETE"))
                .thenReturn(Arrays.asList());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrdersByDateAndStatus(orderRequest.getOrderDate(), "COMPLETE");
        });
        assertNotNull(exception.getMessage());
    }

}