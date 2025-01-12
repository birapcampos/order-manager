package br.com.campos.order.controller.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

        @NotBlank(message = "O nome do produto é obrigatório.")
        private String name;

        @Positive(message = "O preço do produto deve ser maior do que zero.")
        private Double price;
}
