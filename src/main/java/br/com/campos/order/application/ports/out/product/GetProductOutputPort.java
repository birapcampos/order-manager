package br.com.campos.order.application.ports.out.product;

import br.com.campos.order.adapters.out.response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface GetProductOutputPort {

    Optional<ProductResponse> getProduct(Long id);
    List<ProductResponse> getAllProducts();
}
