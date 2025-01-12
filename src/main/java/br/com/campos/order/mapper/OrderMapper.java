package br.com.campos.order.mapper;

import br.com.campos.order.controller.dto.request.OrderItemRequest;
import br.com.campos.order.controller.dto.request.OrderRequest;
import br.com.campos.order.controller.dto.response.OrderResponse;
import br.com.campos.order.controller.dto.response.OrderItemResponse;
import br.com.campos.order.repository.entity.OrderEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static OrderEntity toOrderEntity(OrderRequest orderRequest) {
        return OrderEntity.builder()
                .customerId(orderRequest.getCustomerId())
                .orderDate(formatDateTime(orderRequest.getOrderDate()))
                .status("PENDING")
                .items(mapOrderItems(orderRequest.getItems()))
                .build();
    }

    public static OrderResponse toOrderResponse(OrderEntity orderEntity) {
        return OrderResponse.builder()
                .orderId(orderEntity.getId())
                .orderDate(orderEntity.getOrderDate().toString())
                .orderAmount(orderEntity.getOrderAmount())
                .customerId(orderEntity.getCustomerId())
                .status(orderEntity.getStatus())
                .items(orderEntity.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProductId(),
                                item.getProductPrice(),
                                item.getQuantity()))
                        .collect(Collectors.toList()))
                .build();
    }

    private static List<OrderEntity.OrderItem> mapOrderItems(List<OrderItemRequest> itemRequests) {
        if (itemRequests == null) {
            return null;
        }

        return itemRequests.stream()
                .map(item -> OrderEntity.OrderItem.builder()
                        .productId(item.getProductId())
                        .productPrice(item.getProductPrice())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    private static LocalDate formatDateTime(LocalDate date) {
        return LocalDate.parse(date.format(DATE_FORMATTER), DATE_FORMATTER);
    }
}
