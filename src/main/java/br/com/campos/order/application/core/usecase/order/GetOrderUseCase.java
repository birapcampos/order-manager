package br.com.campos.order.application.core.usecase.order;

import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.ports.in.order.GetOrderInputPort;
import br.com.campos.order.application.ports.out.order.GetOrderOutputPort;

import java.util.List;
import java.util.Optional;

public class GetOrderUseCase implements GetOrderInputPort {

    private GetOrderOutputPort getOrderOutputPort;

    public GetOrderUseCase(GetOrderOutputPort getOrderOutputPort) {

        this.getOrderOutputPort = getOrderOutputPort;
    }

    @Override
    public Optional<OrderResponse> getOrderById(Long id){

        return getOrderOutputPort.getOrderById(id);
    }

    @Override
    public List<OrderResponse> getAllOrders(){

        return getOrderOutputPort.getAllOrders();
    }

}
