package br.com.campos.order.application.ports.out.order;

import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.core.domain.Order;

public interface UpdateOrderOutputPort {

    OrderResponse updateOrder(Long id, Order updatedOrder);
}
