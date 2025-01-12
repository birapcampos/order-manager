package br.com.campos.order.controller;

import br.com.campos.order.controller.dto.request.ProductRequest;
import br.com.campos.order.controller.dto.response.ProductResponse;
import br.com.campos.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final String BASE_URL = "/api/v1/product";
    private final String productId = "123456";
    private final String productName = "Cerveja Brahma 350 ml 12 unidades";
    private final Double productPrice = 24.0;

    @Test
    void testCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest(productName, productPrice);
        ProductResponse response = new ProductResponse(
                productId,
                productName,
                productPrice);

        // Stub do service para retornar o ProductResponse
        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(response);

        // JSON com o formato correto
        String jsonRequest = "{ \"name\": \"" + productName + "\", \"price\": " + productPrice + " }";

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))  // Passando o JSON correto
                .andExpect(status().isCreated())  // Status esperado 201
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(productName))
                .andExpect(jsonPath("$.price").value(productPrice));
    }


    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest(productName, productPrice);
        ProductResponse response = new ProductResponse(
                productId,
                productName,
                productPrice);

        // Stub do service para retornar o ProductResponse atualizado
        when(productService.updateProduct(eq(productId), any(ProductRequest.class)))
                .thenReturn(response);

        // JSON com o formato correto para atualizar o produto
        String jsonRequest = "{ \"name\": \"" + productName + "\", \"price\": " + productPrice + " }";

        mockMvc.perform(put(BASE_URL + "/" + productId)  // Usando o ID no URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))  // Passando o JSON correto
                .andExpect(status().isOk())  // Status esperado 200
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(productName))
                .andExpect(jsonPath("$.price").value(productPrice));
    }



    @Test
    void testGetProductById() throws Exception {
        ProductResponse response = new ProductResponse(productId, productName, productPrice);

        when(productService.getProduct(any(String.class))).thenReturn(Optional.of(response));

        mockMvc.perform(get(BASE_URL + "/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(productName))
                .andExpect(jsonPath("$.price").value(productPrice));
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductResponse response = new ProductResponse(productId, productName, productPrice);
        List<ProductResponse> products = Collections.singletonList(response);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(productId))
                .andExpect(jsonPath("$[0].name").value(productName))
                .andExpect(jsonPath("$[0].price").value(productPrice));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", productId))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(productId);
    }
}
