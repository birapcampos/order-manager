package br.com.campos.order.application.core.usecase.order;

import br.com.campos.order.application.ports.in.order.DeleteOrderInputPort;
import br.com.campos.order.application.ports.out.order.DeleteOrderOutputPort;

public class DeleteOrderUseCase implements DeleteOrderInputPort {

    private DeleteOrderOutputPort deleteOrderOutputPort;

    public DeleteOrderUseCase(DeleteOrderOutputPort deleteOrderOutputPort) {
        this.deleteOrderOutputPort = deleteOrderOutputPort;
    }

    @Override
    public void deleteOrder(Long id){
        deleteOrderOutputPort.deleteOrder(id);
    }

}
