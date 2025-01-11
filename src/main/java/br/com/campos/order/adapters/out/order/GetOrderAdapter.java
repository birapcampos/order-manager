package br.com.campos.order.adapters.out.order;

import br.com.campos.order.adapters.out.repository.OrderRepository;
import br.com.campos.order.adapters.out.repository.entity.OrderEntity;
import br.com.campos.order.adapters.out.response.OrderItemResponse;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.ports.out.order.GetOrderOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GetOrderAdapter implements GetOrderOutputPort {

    private OrderRepository orderRepository;

    public GetOrderAdapter(OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderEntity -> new OrderResponse(orderEntity.getId(),
                        orderEntity.getCustomerName(),
                        orderEntity.getItems().stream()
                                .map(orderItemEntity -> new OrderItemResponse(
                                        orderItemEntity.getId(),
                                        orderItemEntity.getProduct().getId(),
                                        orderItemEntity.getQuantity()))
                                .collect(Collectors.toList())));
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<OrderEntity> orderEntities = orderRepository.findAll();

        return orderEntities.stream()
                .map(orderEntity -> new OrderResponse(orderEntity.getId(), orderEntity.getCustomerName(),
                        orderEntity.getItems().stream()
                                .map(orderItemEntity -> new OrderItemResponse(orderItemEntity.getId(), orderItemEntity.getProduct().getId(), orderItemEntity.getQuantity()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
