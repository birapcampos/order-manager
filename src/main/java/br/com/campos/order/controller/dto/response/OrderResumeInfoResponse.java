package br.com.campos.order.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResumeInfoResponse {

    private String orderId;
    private String orderDate;
    private String status;
    private Long customerId;
    private Double orderAmount;
}
