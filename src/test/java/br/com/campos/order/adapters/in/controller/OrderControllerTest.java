package br.com.campos.order.adapters.in.controller;

import br.com.campos.order.adapters.in.controller.request.OrderRequest;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.core.domain.OrderItem;
import br.com.campos.order.application.core.domain.Product;
import br.com.campos.order.application.ports.in.order.CreateOrderInputPort;
import br.com.campos.order.application.ports.in.order.DeleteOrderInputPort;
import br.com.campos.order.application.ports.in.order.GetOrderInputPort;
import br.com.campos.order.application.ports.in.order.UpdateOrderInputPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateOrderInputPort createOrderInputPort;

    @MockBean
    private UpdateOrderInputPort updateOrderInputPort;

    @MockBean
    private GetOrderInputPort getOrderInputPort;

    @MockBean
    private DeleteOrderInputPort deleteOrderInputPort;

    @Test
    void testCreateOrder() throws Exception {

        Product product = new Product("Geladeira", 1780);
        OrderItem orderItem = new OrderItem(1L, product, 3);
        List<OrderItem> orderItems = Collections.singletonList(orderItem);

        OrderRequest request = new OrderRequest(1L, "Customer", orderItems);
        OrderResponse response = new OrderResponse();

        when(createOrderInputPort.createOrder(any(OrderRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"customerId\": 1, \"items\": [{\"productId\": 1, \"quantity\": 3}] }")) // Corrigido o JSON enviado
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }


    @Test
    void testUpdateOrder() throws Exception {
        OrderResponse response = new OrderResponse();

        when(updateOrderInputPort.updateOrder(any(Long.class),
                any(OrderRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderResponse response = new OrderResponse();

        when(getOrderInputPort.getOrderById(any(Long.class))).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderResponse response = new OrderResponse();

        when(getOrderInputPort.getAllOrders()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/v1/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/v1/order/1"))
                .andExpect(status().isNoContent());

        verify(deleteOrderInputPort).deleteOrder(1L);
    }
}
