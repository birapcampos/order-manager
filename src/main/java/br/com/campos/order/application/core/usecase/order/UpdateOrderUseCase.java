package br.com.campos.order.application.core.usecase.order;

import br.com.campos.order.adapters.in.controller.request.OrderRequest;
import br.com.campos.order.adapters.out.mapper.OrderMapper;
import br.com.campos.order.application.ports.in.order.UpdateOrderInputPort;
import br.com.campos.order.application.ports.out.order.UpdateOrderOutputPort;
import br.com.campos.order.adapters.out.response.OrderResponse;

public class UpdateOrderUseCase implements UpdateOrderInputPort {

    private UpdateOrderOutputPort updateOrderOutputPort;
    private OrderMapper orderMapper;

    public UpdateOrderUseCase(UpdateOrderOutputPort updateOrderOutputPort,
                              OrderMapper orderMapper) {
        this.updateOrderOutputPort = updateOrderOutputPort;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest updatedOrder){
        return updateOrderOutputPort.updateOrder(id,orderMapper.toOrder(updatedOrder));
    }

}
