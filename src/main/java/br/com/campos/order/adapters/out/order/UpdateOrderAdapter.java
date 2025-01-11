package br.com.campos.order.adapters.out.order;

import br.com.campos.order.adapters.out.repository.OrderRepository;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.OrderEntity;
import br.com.campos.order.adapters.out.repository.entity.OrderItemEntity;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.OrderItemResponse;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.core.domain.Order;
import br.com.campos.order.application.ports.out.order.UpdateOrderOutputPort;
import br.com.campos.order.exceptions.OrderNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateOrderAdapter implements UpdateOrderOutputPort {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public UpdateOrderAdapter(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, Order updatedOrder) {

        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id.toString()));

        orderEntity.setCustomerName(updatedOrder.getCustomerName());

        /*
        Cria um mapa das OrderItemEntity existentes na orderEntity atual,
        onde a chave é o ID do produto (orderItemEntity.getProduct().getId())
        e o valor é a própria OrderItemEntity.
        */
        Map<Long, OrderItemEntity> existingItemsMap = orderEntity.getItems().stream()
                .collect(Collectors.toMap(orderItemEntity ->
                                orderItemEntity.getProduct().getId(),
                        orderItemEntity -> orderItemEntity));


        /*
        Mapeia os itens atualizando a updatedOrder para as OrderItemEntity correspondentes
        na orderEntity ou cria novas OrderItemEntity se não existirem.
         */
        List<OrderItemEntity> updatedItems = updatedOrder.getItems().stream()
                .map(orderItem -> {
                    OrderItemEntity existingItem = existingItemsMap.get(orderItem.getProduct().getId());

                    if (existingItem != null) {
                        existingItem.setQuantity(orderItem.getQuantity());
                        return existingItem;
                    } else {
                      ProductEntity productEntity = productRepository.findById(orderItem.getProduct().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + orderItem.getProduct().getId()));

                      return new OrderItemEntity(null, productEntity, orderItem.getQuantity(), orderEntity);
                    }
                })
                .collect(Collectors.toList());

                // Define os itens atualizados na ordem
                orderEntity.setItems(updatedItems);

                // Salva a ordem atualizada no banco de dados
                OrderEntity savedOrder = orderRepository.save(orderEntity);

                return new OrderResponse(savedOrder.getId(), savedOrder.getCustomerName(),
                        savedOrder.getItems().stream()
                                .map(orderItemEntity -> new OrderItemResponse(
                                        orderItemEntity.getId(),
                                        orderItemEntity.getProduct().getId(),
                                        orderItemEntity.getQuantity()))
                                .collect(Collectors.toList()));
    }
}
