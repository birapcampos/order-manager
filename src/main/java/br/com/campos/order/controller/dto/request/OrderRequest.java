package br.com.campos.order.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "O id do cliente deve ser informado.")
    private Long customerId;

    @NotNull(message = "A data do pedido deve ser informada.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @Size(min = 1, message = "A lista de itens não pode estar vazia.")
    @Valid
    private List<OrderItemRequest> items;
}
