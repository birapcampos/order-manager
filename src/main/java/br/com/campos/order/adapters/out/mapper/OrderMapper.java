package br.com.campos.order.adapters.out.mapper;

import br.com.campos.order.adapters.in.controller.request.OrderRequest;
import br.com.campos.order.application.core.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toOrder(OrderRequest orderRequest){
        return new Order(orderRequest.id(),orderRequest.customerName(),orderRequest.items());
    }

}
