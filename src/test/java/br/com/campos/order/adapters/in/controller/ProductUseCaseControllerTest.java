package br.com.campos.order.adapters.in.controller;

import br.com.campos.order.adapters.in.controller.request.ProductRequest;
import br.com.campos.order.adapters.out.response.ProductResponse;
import br.com.campos.order.application.ports.in.product.ProductInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
class ProductUseCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductInputPort productInputPort;

    private ProductRequest request;
    private ProductResponse response;

    @BeforeEach
    void setUp(){
        request = new ProductRequest(1L, "Product", 1000);
        response = new ProductResponse(1L, "Product", 1000);
    }

    @Test
    void testCreateProduct() throws Exception {

        when(productInputPort.create(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Product\", \"price\": 1000 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product"))
                .andExpect(jsonPath("$.price").value(1000));
    }

    @Test
    void testGetProduct() throws Exception {

        when(productInputPort.getProduct(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Product"))
                .andExpect(jsonPath("$.price").value(1000));
    }

    @Test
    void testGetAllProducts() throws Exception {

        when(productInputPort.getAllProducts()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product"))
                .andExpect(jsonPath("$[0].price").value(1000));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/product/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateProduct() throws Exception {

        ProductRequest requestUpdate = new ProductRequest(1L, "Geladeira", 3150);
        ProductResponse responseUpdate = new ProductResponse(1L, "Geladeira", 3150);

        when(productInputPort.updateProduct(1L, requestUpdate)).thenReturn(responseUpdate);

        mockMvc.perform(put("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Geladeira\", \"price\": 3150 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Geladeira"))
                .andExpect(jsonPath("$.price").value(3150));
    }
}
