package br.com.campos.order.adapters.out.product;

import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductAdapterTestUseCase {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private GetProductAdapter getProductAdapter;

    private ProductEntity productEntity;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Product 1");
        productEntity.setPrice(1000);

        productResponse = new ProductResponse();
    }

    @Test
    void testGetProduct_Success() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(productEntity));
        when(productMapper.toProductResponse(any(ProductEntity.class))).thenReturn(productResponse);

        Optional<ProductResponse> response = getProductAdapter.getProduct(1L);

        verify(productRepository).findById(1L);
        verify(productMapper).toProductResponse(productEntity);

        assertTrue(response.isPresent());
        assertEquals(productResponse, response.get());
    }

    @Test
    void testGetProduct_ProductNotFound() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            getProductAdapter.getProduct(1L);
        });

        String expectedMessage = "Produto não encontrado com este Id: 1";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(productRepository).findById(1L);
        verify(productMapper, never()).toProductResponse(any(ProductEntity.class));
    }

    @Test
    void testGetAllProducts_Success() {
        List<ProductEntity> productEntities = Arrays.asList(productEntity);
        List<ProductResponse> productResponses = Arrays.asList(productResponse);

        when(productRepository.findAll()).thenReturn(productEntities);
        when(productMapper.toProductResponse(any(ProductEntity.class))).thenReturn(productResponse);

        List<ProductResponse> responses = getProductAdapter.getAllProducts();

        verify(productRepository).findAll();
        verify(productMapper, times(productEntities.size())).toProductResponse(any(ProductEntity.class));

        assertNotNull(responses);
        assertEquals(productResponses.size(), responses.size());
        assertEquals(productResponses, responses);
    }
}
