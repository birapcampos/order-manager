package br.com.campos.order.controller;

import br.com.campos.order.controller.dto.request.OrderItemRequest;
import br.com.campos.order.controller.dto.request.OrderRequest;
import br.com.campos.order.controller.dto.response.OrderItemResponse;
import br.com.campos.order.controller.dto.response.OrderResponse;
import br.com.campos.order.controller.dto.response.OrderResumeInfoResponse;
import br.com.campos.order.controller.dto.response.OrderSummaryResponse;
import br.com.campos.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String orderId = "333";
    private final Long customerId = 123L;
    private final LocalDate orderDateReq = formatDateTime(LocalDate.now());
    private final String orderDateResp = formatDateTime(LocalDate.now()).toString();
    private final Double orderAmount = 0.0;
    private static final String BASE_URL = "/api/v1/order";

    @Test
    void testCreateOrder() throws Exception {

        OrderRequest orderRequest = new OrderRequest(customerId,orderDateReq, List.of(
                new OrderItemRequest("A1", 25.5, 2)
        ));
        String content = objectMapper.writeValueAsString(orderRequest);

        OrderResponse mockResponse = new OrderResponse(
                orderId,
                orderDateResp,
                "PENDING",
                customerId,
                orderAmount,
                List.of(new OrderItemResponse("1",25.5, 2))
        );
        Mockito.when(orderService.createOrder(Mockito.any(OrderRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderDate").value(orderDateResp))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.orderAmount").value(orderAmount))
                .andExpect(jsonPath("$.items[0].productId").value("1"))
                .andExpect(jsonPath("$.items[0].productPrice").value(25.5))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }


    @Test
    void testGetAllOrders() throws Exception {

        OrderResponse response = new OrderResponse(
                orderId,
                orderDateResp,
                "PENDING",
                customerId,
                orderAmount,
                List.of(new OrderItemResponse("1", 1780.0, 3))
        );

        when(orderService.getAllOrders(orderDateReq, null)).thenReturn(List.of(response));

        mockMvc.perform(get(BASE_URL)
                        .param("orderDate", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(orderId))
                .andExpect(jsonPath("$[0].orderDate").value(orderDateResp))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].customerId").value(customerId))
                .andExpect(jsonPath("$[0].orderAmount").value(orderAmount))
                .andExpect(jsonPath("$[0].items[0].productId").value("1"))
                .andExpect(jsonPath("$[0].items[0].productPrice").value(1780.0))
                .andExpect(jsonPath("$[0].items[0].quantity").value(3));
    }

    @Test
    void testGetAllOrdersWithFilters() throws Exception {
        OrderResponse response = new OrderResponse(
                orderId,
                orderDateResp,
                "COMPLETED",
                customerId,
                orderAmount,
                List.of(new OrderItemResponse("2", 100.0, 1))
        );

        when(orderService.getAllOrders(orderDateReq, "COMPLETED"))
                .thenReturn(List.of(response));

        mockMvc.perform(get(BASE_URL)
                .param("orderDate", LocalDate.now().toString())
                .param("status", "COMPLETED")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(orderId))
                .andExpect(jsonPath("$[0].orderDate").value(orderDateResp))
                .andExpect(jsonPath("$[0].orderAmount").value(orderAmount))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"));

    }

    @Test
    void testGetResumeOrdersInfoWithTotalAmount() throws Exception {

        List<OrderResumeInfoResponse> mockOrders = List.of(
                new OrderResumeInfoResponse("6783e586106be90eeaf417f2",
                        "2025-01-12",
                        "COMPLETE",
                        874L,
                        17381.0),
                new OrderResumeInfoResponse("6783efd183732a681bf426f4",
                        "2025-01-12",
                        "COMPLETE",
                        874L,
                        21180.0)
        );

        double mockTotalAmount = 38561.0;

        when(orderService.getResumeOrdersInfo(orderDateReq, "COMPLETE"))
                .thenReturn(new OrderSummaryResponse(mockOrders, mockTotalAmount));

        mockMvc.perform(get(BASE_URL + "/summary")
                        .param("orderDate", orderDateReq.toString())
                        .param("status", "COMPLETE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderId").value("6783e586106be90eeaf417f2"))
                .andExpect(jsonPath("$.orders[0].orderDate").value("2025-01-12"))
                .andExpect(jsonPath("$.orders[0].status").value("COMPLETE"))
                .andExpect(jsonPath("$.orders[0].customerId").value(874))
                .andExpect(jsonPath("$.orders[0].orderAmount").value(17381.0))
                .andExpect(jsonPath("$.orders[1].orderId").value("6783efd183732a681bf426f4"))
                .andExpect(jsonPath("$.orders[1].orderDate").value("2025-01-12"))
                .andExpect(jsonPath("$.orders[1].status").value("COMPLETE"))
                .andExpect(jsonPath("$.orders[1].customerId").value(874))
                .andExpect(jsonPath("$.orders[1].orderAmount").value(21180.0))
                .andExpect(jsonPath("$.totalAmount").value(mockTotalAmount));
    }


    @Test
    void testGetOrder() throws Exception {

        OrderResponse orderResponse = new OrderResponse(
                orderId,
                orderDateResp,
                "PENDING",
                customerId,
                orderAmount,
                List.of(new OrderItemResponse("1", 25.5, 2))
        );

        when(orderService.getOrder(orderId)).thenReturn(Optional.of(orderResponse));

        mockMvc.perform(get(BASE_URL + "/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderDate").value(orderDateResp))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.orderAmount").value(orderAmount))
                .andExpect(jsonPath("$.items[0].productId").value("1"))
                .andExpect(jsonPath("$.items[0].productPrice").value(25.5))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    void testGetOrderNotFound() throws Exception {

        when(orderService.getOrder(orderId)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    private static LocalDate formatDateTime(LocalDate date) {
       return LocalDate.parse(date.format(DATE_FORMATTER), DATE_FORMATTER);
    }

}
