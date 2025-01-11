package br.com.campos.order.application.core.usecase.order;

import br.com.campos.order.adapters.in.controller.request.OrderRequest;
import br.com.campos.order.adapters.out.mapper.OrderMapper;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.ports.in.order.CreateOrderInputPort;
import br.com.campos.order.application.ports.out.order.CreateOrderOutputPort;

public class CreateOrderUseCase implements CreateOrderInputPort {

    private CreateOrderOutputPort createOrderOutputPort;
    private OrderMapper orderMapper;

    public CreateOrderUseCase(CreateOrderOutputPort createOrderOutputPort,
                              OrderMapper orderMapper) {
        this.createOrderOutputPort = createOrderOutputPort;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponse createOrder(OrderRequest order) {

        return createOrderOutputPort.createOrder(orderMapper.toOrder(order));
    }
}
