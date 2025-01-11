package br.com.campos.order.adapters.out.order;

import br.com.campos.order.adapters.out.repository.OrderRepository;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.OrderEntity;
import br.com.campos.order.adapters.out.repository.entity.OrderItemEntity;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.OrderItemResponse;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.core.domain.Order;
import br.com.campos.order.application.ports.out.order.CreateOrderOutputPort;
import br.com.campos.order.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CreateOrderAdapter implements CreateOrderOutputPort {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    public CreateOrderAdapter(
            ProductRepository productRepository,
            OrderRepository orderRepository) {

        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Order order) {

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerName(order.getCustomerName());

        List<OrderItemEntity> orderItemEntities = order.getItems().stream()
                .map(orderItem -> {
                    ProductEntity productEntity = productRepository.findById(orderItem.getProduct().getId())
                            .orElseThrow(() -> new ProductNotFoundException(orderItem.getProduct().getId()
                                    .toString()));
                    return new OrderItemEntity(null, productEntity, orderItem.getQuantity(), orderEntity);
                })
                .collect(Collectors.toList());

        orderEntity.setItems(orderItemEntities);

        OrderEntity savedOrder = orderRepository.save(orderEntity);

        return new OrderResponse(savedOrder.getId(), savedOrder.getCustomerName(),
                savedOrder.getItems().stream()
                        .map(orderItemEntity -> new OrderItemResponse(
                                savedOrder.getId(),
                                orderItemEntity.getProduct().getId(),
                                orderItemEntity.getQuantity()))
                        .collect(Collectors.toList()));
    }
}
