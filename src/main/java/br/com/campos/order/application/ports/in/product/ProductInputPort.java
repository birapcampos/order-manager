package br.com.campos.order.application.ports.in.product;

import br.com.campos.order.adapters.in.controller.request.ProductRequest;
import br.com.campos.order.adapters.out.response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductInputPort {
    ProductResponse create(ProductRequest product);
    Optional<ProductResponse> getProduct(Long id);
    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(Long id, ProductRequest product);

    void deleteProduct(Long id);
}
