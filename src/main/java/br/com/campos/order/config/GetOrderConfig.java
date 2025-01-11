package br.com.campos.order.config;

import br.com.campos.order.adapters.out.order.GetOrderAdapter;
import br.com.campos.order.application.core.usecase.order.GetOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetOrderConfig {
    @Bean
    public GetOrderUseCase getOrderUseCase(GetOrderAdapter getOrderOutputPort){
        return new GetOrderUseCase(getOrderOutputPort);
    }
}
