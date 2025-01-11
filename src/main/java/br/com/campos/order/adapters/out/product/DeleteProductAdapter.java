package br.com.campos.order.adapters.out.product;

import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.application.ports.out.product.DeleteProductOutputPort;
import br.com.campos.order.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class DeleteProductAdapter implements DeleteProductOutputPort {

    private ProductRepository productRepository;
    private ProductMapper productMapper;

    public DeleteProductAdapter(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void deleteProduct(Long id) {
        Optional<ProductEntity> productEntityOptional = productRepository.findById(id);
        if (!productEntityOptional.isPresent()) {
            throw new ProductNotFoundException(id.toString());
        }
        productRepository.deleteById(id);
    }

}
