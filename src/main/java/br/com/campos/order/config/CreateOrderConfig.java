package br.com.campos.order.config;

import br.com.campos.order.adapters.out.mapper.OrderMapper;
import br.com.campos.order.adapters.out.order.CreateOrderAdapter;
import br.com.campos.order.application.core.usecase.order.CreateOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateOrderConfig {

    @Bean
    public CreateOrderUseCase createOrderUseCase(CreateOrderAdapter createOrderOutputPort,
                                                 OrderMapper orderMapper){

        return new CreateOrderUseCase(createOrderOutputPort,orderMapper);
    }

}
