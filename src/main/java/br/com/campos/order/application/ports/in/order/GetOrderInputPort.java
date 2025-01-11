package br.com.campos.order.application.ports.in.order;

import br.com.campos.order.adapters.out.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface GetOrderInputPort {

    Optional<OrderResponse> getOrderById(Long id);

    List<OrderResponse> getAllOrders();

}
