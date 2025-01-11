package br.com.campos.order.application.ports.in.order;

import br.com.campos.order.adapters.in.controller.request.OrderRequest;
import br.com.campos.order.adapters.out.response.OrderResponse;

public interface UpdateOrderInputPort {

    OrderResponse updateOrder(Long id, OrderRequest updatedOrder);
}
