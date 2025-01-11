package br.com.campos.order.application.ports.out.order;

import br.com.campos.order.adapters.out.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface GetOrderOutputPort {

    Optional<OrderResponse> getOrderById(Long id);
    List<OrderResponse> getAllOrders();
}
