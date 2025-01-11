package br.com.campos.order.adapters.out.mapper;

import br.com.campos.order.adapters.in.controller.request.ProductRequest;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.core.domain.Product;
import org.springframework.stereotype.Component;
@Component
public class ProductMapper {

    public ProductResponse toProductResponse(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getPrice());
    }

    public ProductEntity toEntity(Product product){
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice());
    }

    public Product toProduct(ProductRequest productRequest){
        return new Product(
                productRequest.name(),
                productRequest.price());
    }

}
