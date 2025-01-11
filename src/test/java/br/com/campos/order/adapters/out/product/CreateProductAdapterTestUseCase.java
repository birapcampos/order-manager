package br.com.campos.order.adapters.out.product;

import br.com.campos.order.adapters.in.controller.request.ProductRequest;
import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.core.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateProductAdapterTestUseCase {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CreateProductAdapter createProductAdapter;

    private ProductRequest productRequest;

    private Product product;
    private ProductResponse productResponse;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {

        product = new Product("Geladeira", 1500);
        productRequest = new ProductRequest(1L, "Geladeira", 1500);
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productResponse = new ProductResponse();
    }

    @Test
    void testCreate() {
        lenient().when(productMapper.toEntity(any(Product.class))).thenReturn(productEntity);
        lenient().when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        lenient().when(productMapper.toProductResponse(any(ProductEntity.class))).thenReturn(productResponse);

        ProductResponse response = createProductAdapter.create(product);

        verify(productMapper).toEntity(product);
        verify(productRepository).save(productEntity);
        verify(productMapper).toProductResponse(productEntity);

        assertNotNull(response);
        assertEquals(productResponse, response);
    }
}
