package br.com.campos.order.adapters.in.controller.request;

import br.com.campos.order.application.core.domain.OrderItem;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest (

    @Transient
    Long id,
    @Size(min=3,max=50,message = "Deve ter entre 3 e 50 characters.")
    @NotBlank(message = "Não pode ser branco ou nulo.")
    String customerName,

    @Min.List(@Min(1))
    List<OrderItem> items

) {}

