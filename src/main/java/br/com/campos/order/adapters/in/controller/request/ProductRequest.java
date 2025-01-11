package br.com.campos.order.adapters.in.controller.request;


import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequest (

        @Transient
        Long id,

        @Size(min=3,max=50,message = "Deve ter entre 3 e 50 characters.")
        @NotBlank(message = "Não pode ser branco ou nulo.")
        String name,

        @Positive(message = "Deve maior do que zero.")
        double price

){}
