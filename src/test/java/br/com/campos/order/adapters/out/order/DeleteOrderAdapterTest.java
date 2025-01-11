package br.com.campos.order.adapters.out.order;

import br.com.campos.order.adapters.out.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteOrderAdapterTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DeleteOrderAdapter deleteOrderAdapter;

    @Test
    void testDeleteOrder() {

        doNothing().when(orderRepository).deleteById(1L);

        deleteOrderAdapter.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }
}
