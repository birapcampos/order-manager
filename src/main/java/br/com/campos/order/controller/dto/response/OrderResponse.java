package br.com.campos.order.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private String orderId;
    private String orderDate;
    private String status;
    private Long customerId;
    private Double orderAmount;
    private List<OrderItemResponse> items;

}
