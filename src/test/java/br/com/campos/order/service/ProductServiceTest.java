package br.com.campos.order.service;

import br.com.campos.order.controller.dto.request.ProductRequest;
import br.com.campos.order.controller.dto.response.ProductResponse;
import br.com.campos.order.repository.ProductRepository;
import br.com.campos.order.repository.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private ProductEntity productEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        productRequest = ProductRequest.builder()
                .name("Produto Teste")
                .price(100.0)
                .build();

        productEntity = ProductEntity.builder()
                .id("prod-001")
                .name("Produto Teste")
                .price(100.0)
                .build();
    }

    @Test
    public void testCreateProduct_Success() {
        // Arrange
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Act
        ProductResponse productResponse = productService.createProduct(productRequest);

        // Assert
        assertNotNull(productResponse);
        assertEquals("prod-001", productResponse.getId());
        assertEquals("Produto Teste", productResponse.getName());
    }

    @Test
    public void testUpdateProduct_Success() {
        // Arrange
        when(productRepository.findById("prod-001")).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Act
        ProductResponse productResponse = productService.updateProduct("prod-001", productRequest);

        // Assert
        assertNotNull(productResponse);
        assertEquals("prod-001", productResponse.getId());
    }

    @Test
    public void testUpdateProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById("prod-001")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct("prod-001", productRequest);
        });
        assertEquals("Produto não encontrado com ID: prod-001", exception.getMessage());
    }
}