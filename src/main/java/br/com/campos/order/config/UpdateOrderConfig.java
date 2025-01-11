package br.com.campos.order.config;

import br.com.campos.order.adapters.out.mapper.OrderMapper;
import br.com.campos.order.adapters.out.order.UpdateOrderAdapter;
import br.com.campos.order.application.core.usecase.order.UpdateOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateOrderConfig {

    @Bean
    public UpdateOrderUseCase updateOrderUseCase(UpdateOrderAdapter updateOrderOutputPort,
                                                 OrderMapper orderMapper){
        return new UpdateOrderUseCase(updateOrderOutputPort,orderMapper);
    }

}
