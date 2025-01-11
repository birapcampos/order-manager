package br.com.campos.order.adapters.out.order;

import br.com.campos.order.adapters.in.controller.request.OrderRequest;
import br.com.campos.order.adapters.out.repository.OrderRepository;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.OrderEntity;
import br.com.campos.order.adapters.out.repository.entity.OrderItemEntity;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.OrderItemResponse;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.core.domain.Order;
import br.com.campos.order.application.core.domain.OrderItem;
import br.com.campos.order.application.core.domain.Product;
import br.com.campos.order.exceptions.ProductNotFoundException;
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
class CreateOrderAdapterTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CreateOrderAdapter createOrderAdapter;


    private Order order;
    private OrderRequest orderRequest;
    private ProductEntity productEntity;
    private OrderEntity orderEntity;

    private final String cliente = "Cliente XXX";
    private final Long productId = 1L;
    private final Long orderId = 1L;

    @BeforeEach
    void setUp() {

        productEntity = new ProductEntity();
        productEntity.setId(productId);

        Product product = new Product();
        product.setId(productId);

        OrderItem orderItem = new OrderItem(productId, product, 2);

        order = new Order(productId, cliente,
                Collections.singletonList(orderItem));

        //orderRequest = new OrderRequest(productId, cliente,
        //        Collections.singletonList(orderItem));

        OrderItemEntity orderItemEntity = new OrderItemEntity(
                null, productEntity, orderItem.getQuantity(), null);

        orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setCustomerName(cliente);
        orderEntity.setItems(Collections.singletonList(orderItemEntity));
    }

    @Test
    void testCreateOrder_Success() {

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse orderResponse = createOrderAdapter.createOrder(order);

        assertNotNull(orderResponse);
        assertEquals(cliente, orderResponse.getCustomerName());

        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testCreateOrder_ProductNotFound() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                createOrderAdapter.createOrder(order));

        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(0)).save(any(OrderEntity.class));
    }

    @Test
    void testCreateOrder_ProductFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse orderResponse = createOrderAdapter.createOrder(order);
        assertNotNull(orderResponse);
        assertEquals(cliente, orderResponse.getCustomerName());

        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testCreateOrder_OrderResponseItems() {

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse orderResponse = createOrderAdapter.createOrder(order);

        assertNotNull(orderResponse);
        assertEquals(cliente, orderResponse.getCustomerName());

        assertEquals(1, orderResponse.getItems().size());
        OrderItemResponse orderItemResponse = orderResponse.getItems().get(0);
        assertEquals(productId, orderItemResponse.getProductId());
        assertEquals(2, orderItemResponse.getQuantity());

        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

}
