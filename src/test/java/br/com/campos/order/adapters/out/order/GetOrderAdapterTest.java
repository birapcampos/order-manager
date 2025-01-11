package br.com.campos.order.adapters.out.order;

import br.com.campos.order.adapters.out.repository.OrderRepository;
import br.com.campos.order.adapters.out.repository.entity.OrderEntity;
import br.com.campos.order.adapters.out.repository.entity.OrderItemEntity;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.OrderItemResponse;
import br.com.campos.order.adapters.out.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrderAdapterTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderAdapter getOrderAdapter;

    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCustomerName("Customer1");
        orderEntity.setItems(Collections.emptyList());
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        Optional<OrderResponse> orderResponseOptional = getOrderAdapter.getOrderById(1L);

        assertTrue(orderResponseOptional.isPresent());
        OrderResponse orderResponse = orderResponseOptional.get();
        assertEquals(orderEntity.getCustomerName(), orderResponse.getCustomerName());
        assertEquals(orderEntity.getItems().size(), orderResponse.getItems().size());

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllOrders_Success() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(orderEntity));

        List<OrderResponse> orderResponses = getOrderAdapter.getAllOrders();

        assertEquals(1, orderResponses.size());
        OrderResponse orderResponse = orderResponses.get(0);
        assertEquals(orderEntity.getCustomerName(), orderResponse.getCustomerName());
        assertEquals(orderEntity.getItems().size(), orderResponse.getItems().size());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById_OrderFound() {
        // Given
        long orderId = 1L;
        String customerName = "cliente 001";
        long productId = 1L;
        int quantity = 2;

        OrderEntity orderEntity = OrderEntity.builder()
                .id(orderId)
                .customerName(customerName)
                .items(Collections.singletonList(
                        OrderItemEntity.builder()
                                .product(ProductEntity.builder().id(productId).build())
                                .quantity(quantity)
                                .build()))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        // When
        Optional<OrderResponse> orderResponseOptional = getOrderAdapter.getOrderById(orderId);

        // Then
        assertTrue(orderResponseOptional.isPresent());
        OrderResponse orderResponse = orderResponseOptional.get();
        assertEquals(customerName, orderResponse.getCustomerName());
        assertEquals(1, orderResponse.getItems().size());

        OrderItemResponse orderItemResponse = orderResponse.getItems().get(0);
        assertEquals(productId, orderItemResponse.getProductId());
        assertEquals(quantity, orderItemResponse.getQuantity());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetAllOrders_EmptyList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderResponse> orderResponses = getOrderAdapter.getAllOrders();

        assertTrue(orderResponses.isEmpty());

        verify(orderRepository, times(1)).findAll();
    }
}
