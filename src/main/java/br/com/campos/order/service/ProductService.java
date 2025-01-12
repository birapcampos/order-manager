package br.com.campos.order.service;

import br.com.campos.order.controller.dto.request.ProductRequest;
import br.com.campos.order.controller.dto.response.ProductResponse;
import br.com.campos.order.repository.ProductRepository;
import br.com.campos.order.repository.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        ProductEntity productEntity = ProductEntity.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        ProductEntity savedProduct = productRepository.save(productEntity);

        return toProductResponse(savedProduct);
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        Optional<ProductEntity> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            ProductEntity productEntity = existingProduct.get();
            productEntity.setName(productRequest.getName());
            productEntity.setPrice(productRequest.getPrice());

            ProductEntity updatedProduct = productRepository.save(productEntity);
            return toProductResponse(updatedProduct);
        } else {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProduct(String id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        return productEntity.map(this::toProductResponse);
    }

    private ProductResponse toProductResponse(ProductEntity productEntity) {
        return ProductResponse.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .build();
    }
}
