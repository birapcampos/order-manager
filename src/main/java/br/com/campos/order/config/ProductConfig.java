package br.com.campos.order.config;

import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.application.core.usecase.product.ProductUseCase;
import br.com.campos.order.adapters.out.product.CreateProductAdapter;
import br.com.campos.order.adapters.out.product.DeleteProductAdapter;
import br.com.campos.order.adapters.out.product.GetProductAdapter;
import br.com.campos.order.adapters.out.product.UpdateProductAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    public ProductUseCase productUseCase(CreateProductAdapter createProductOutputPort,
                                         UpdateProductAdapter updateProductOutputPort,
                                         GetProductAdapter getProductOutputPort,
                                         DeleteProductAdapter deleteProductOutputPort,
                                         ProductMapper productMapper){

        return new ProductUseCase(createProductOutputPort,
                updateProductOutputPort,
                getProductOutputPort,
                deleteProductOutputPort,
                productMapper);
    }

}
