package br.com.campos.order.repository.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Document(collection = "orders")
public class OrderEntity {

    @Id
    private String id;

    @NotBlank
    private Long customerId;

    @NotBlank
    private LocalDate orderDate;

    private String status;

    private List<OrderItem> items;

    private Double orderAmount;

    @Data
    @Builder
    public static class OrderItem {
        private String productId;
        private double productPrice;
        private int quantity;
    }
}
