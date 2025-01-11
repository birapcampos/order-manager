package br.com.campos.order.adapters.out.product;

import br.com.campos.order.adapters.in.controller.request.ProductRequest;
import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.core.domain.Product;
import br.com.campos.order.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductAdapterTestUseCase {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private UpdateProductAdapter updateProductAdapter;

    private ProductRequest productRequest;

    private Product product;
    private ProductResponse productResponse;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        product = new Product("Updated Product", 2000);
        productRequest = new ProductRequest(1L, "Updated Product", 2000);
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Original Product");
        productEntity.setPrice(1000);
        productResponse = new ProductResponse();
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        when(productMapper.toProductResponse(any(ProductEntity.class))).thenReturn(productResponse);

        ProductResponse response = updateProductAdapter.updateProduct(1L, product);

        verify(productRepository).findById(1L);
        verify(productRepository).save(productEntity);
        verify(productMapper).toProductResponse(productEntity);

        assertNotNull(response);
        assertEquals(productResponse, response);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            updateProductAdapter.updateProduct(1L, product);
        });

        String expectedMessage = "Produto não encontrado com este Id: 1";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(ProductEntity.class));
        verify(productMapper, never()).toProductResponse(any(ProductEntity.class));
    }

}
