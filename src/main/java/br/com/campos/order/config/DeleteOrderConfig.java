package br.com.campos.order.config;

import br.com.campos.order.adapters.out.order.DeleteOrderAdapter;
import br.com.campos.order.application.core.usecase.order.DeleteOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteOrderConfig {

    @Bean
    public DeleteOrderUseCase deleteOrderUseCase(
            DeleteOrderAdapter deleteOrderOutputPortImpl){
       return new DeleteOrderUseCase(deleteOrderOutputPortImpl);
    }

}
