package br.com.campos.order.adapters.out.order;

import br.com.campos.order.adapters.out.repository.OrderRepository;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.OrderEntity;
import br.com.campos.order.adapters.out.repository.entity.OrderItemEntity;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.core.domain.Order;
import br.com.campos.order.application.core.domain.OrderItem;
import br.com.campos.order.application.core.domain.Product;
import br.com.campos.order.exceptions.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderAdapterTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    ProductEntity entity = new ProductEntity();

    @InjectMocks
    private UpdateOrderAdapter updateOrderAdapter;

    private OrderEntity orderEntity;

    private Order order;
    private final Long productId = 1L;
    private final Long orderId = 1L;
    private final Long orderItemId = 1L;

    @BeforeEach
    void setUp() {
        ProductEntity productEntity = ProductEntity.builder().id(productId).build();

        OrderItemEntity orderItemEntity = OrderItemEntity.builder()
                .id(1L)
                .product(productEntity)
                .quantity(2)
                .build();

        orderEntity = OrderEntity.builder()
                .id(orderId)
                .customerName("Customer1")
                .items(Collections.singletonList(orderItemEntity))
                .build();

        Product product = new Product();
        product.setId(productId);

        OrderItem orderItem = new OrderItem(orderItemId, product, 2);

        order = new Order(orderId,"Customer2",
                Collections.singletonList(orderItem));

    }

    @Test
    void testUpdateOrder_Success() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse orderResponse = updateOrderAdapter.updateOrder(orderId, order);

        assertNotNull(orderResponse);
        assertEquals("Customer2", orderResponse.getCustomerName());
        assertEquals(1, orderResponse.getItems().size());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testUpdateOrder_OrderNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () ->
                updateOrderAdapter.updateOrder(orderId, order));

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(0)).save(any(OrderEntity.class));
    }

    @Test
    void testUpdateOrder_DifferentItems_Success() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse orderResponse = updateOrderAdapter.updateOrder(orderId, order);

        assertNotNull(orderResponse);
        assertEquals("Customer2", orderResponse.getCustomerName());
        assertEquals(1, orderResponse.getItems().size());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }


}