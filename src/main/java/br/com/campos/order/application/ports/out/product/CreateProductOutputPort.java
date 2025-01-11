package br.com.campos.order.application.ports.out.product;

import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.core.domain.Product;

public interface CreateProductOutputPort {
    ProductResponse create(Product product);
}
