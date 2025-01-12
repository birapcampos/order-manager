package br.com.campos.order.controller.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    @NotNull(message = "O código do produto é obrigatório.")
    private String productId;

    @Positive(message = "O preço do produto deve ser maior do que zero.")
    private Double productPrice;

    @Positive(message = "O quantidade do produto deve ser maior do que zero.")
    private Integer quantity;
}
