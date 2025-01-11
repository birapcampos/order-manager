package br.com.campos.order.adapters.out.product;

import br.com.campos.order.adapters.out.mapper.ProductMapper;
import br.com.campos.order.adapters.out.repository.ProductRepository;
import br.com.campos.order.adapters.out.repository.entity.ProductEntity;
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
class DeleteProductAdapterTestUseCase {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private DeleteProductAdapter deleteProductAdapter;

    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Product 1");
        productEntity.setPrice(1000);
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(productEntity));

        deleteProductAdapter.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            deleteProductAdapter.deleteProduct(1L);
        });

        String expectedMessage = "Produto não encontrado com este Id: 1";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        verify(productRepository).findById(1L);
        verify(productRepository, never()).deleteById(any(Long.class));
    }
}
