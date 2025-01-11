package br.com.campos.order.adapters.out.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderResponse {

    private Long orderId;
    private String customerName;
    private List<OrderItemResponse> items;
}
