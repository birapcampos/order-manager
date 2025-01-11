package br.com.campos.order.adapters.out.product;

import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.ports.out.product.GetProductOutputPort;
import br.com.campos.order.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class GetProductAdapter implements GetProductOutputPort {

    private ProductRepository productRepository;
    private ProductMapper productMapper;

    public GetProductAdapter(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<ProductResponse> getProduct(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if (!productEntity.isPresent()) {
            throw new ProductNotFoundException(id.toString());
        }
        return productEntity.map(productMapper::toProductResponse);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
