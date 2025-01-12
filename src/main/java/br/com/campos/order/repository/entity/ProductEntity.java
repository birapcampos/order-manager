package br.com.campos.order.repository.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@Document(collection = "products")
public class ProductEntity {

    @Id
    private String id;

    @NotBlank
    private String name;

    @PositiveOrZero
    private double price;
}
