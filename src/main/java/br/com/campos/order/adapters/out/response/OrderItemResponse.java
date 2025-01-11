package br.com.campos.order.adapters.out.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    private Long itemId;
    private Long productId;
    private int quantity;

    public OrderItemResponse(Long itemId, Long productId, int quantity) {
        this.itemId = itemId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
