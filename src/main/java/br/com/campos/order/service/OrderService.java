package br.com.campos.order.service;

import br.com.campos.order.controller.dto.request.OrderItemRequest;
import br.com.campos.order.controller.dto.request.OrderRequest;
import br.com.campos.order.controller.dto.response.OrderResponse;
import br.com.campos.order.controller.dto.response.OrderResumeInfoResponse;
import br.com.campos.order.controller.dto.response.OrderSummaryResponse;
import br.com.campos.order.exceptions.OrderNotFoundException;
import br.com.campos.order.exceptions.ProductDuplicatedException;
import br.com.campos.order.mapper.OrderMapper;
import br.com.campos.order.repository.OrderRepository;
import br.com.campos.order.repository.ProductRepository;
import br.com.campos.order.repository.entity.OrderEntity;
import br.com.campos.order.repository.entity.ProductEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {

        validateDuplicateItems(orderRequest.getItems());
        validateProductsExist(orderRequest.getItems());

        OrderEntity orderEntity = OrderMapper.toOrderEntity(orderRequest);
        Double total = calculateTotal(orderRequest.getItems());
        orderEntity.setOrderAmount(total);
        orderEntity.setStatus("COMPLETE");
        orderEntity = orderRepository.save(orderEntity);
        return OrderMapper.toOrderResponse(orderEntity);
    }

    private void validateDuplicateItems(List<OrderItemRequest> items) {
        long count = items.stream()
                .map(item -> item.getProductId())
                .distinct()
                .count();

        if (count != items.size()) {
            throw new ProductDuplicatedException("Há produtos repetidos na lista.");
        }
    }

    private void validateProductsExist(List<OrderItemRequest> items) {
        List<String> productIds = items.stream()
                .map(OrderItemRequest::getProductId)
                .distinct()
                .toList();

        List<String> existingProductIds = productRepository.findAllById(productIds).stream()
                .map(ProductEntity::getId)
                .toList();

        List<String> missingProducts = productIds.stream()
                .filter(productId -> !existingProductIds.contains(productId))
                .toList();

        if (!missingProducts.isEmpty()) {
            throw new IllegalArgumentException("Os seguintes produtos não existem: " + missingProducts);
        }
    }

    public List<OrderResponse> getOrdersByDateAndStatus(LocalDate orderDate, String orderStatus) {

        // Assume o status COMPLETE caso não informado
        orderStatus = orderStatus.isEmpty() ? "COMPLETE" : orderStatus;

        List<OrderEntity> orders = orderRepository.findByOrderDateAndStatus(orderDate, orderStatus);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Pedido não encontrado com os parâmetros fornecidos.");
        }

        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }


    public Optional<OrderResponse> getOrder(String id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        return orderEntity.map(OrderMapper::toOrderResponse);
    }

    public List<OrderResponse> getAllOrders(LocalDate orderDate, String status) {
        List<OrderEntity> orders;

        // Assume o status COMPLETE caso não informado
        status = status.isEmpty() ? "COMPLETE" : status;

        if (orderDate != null && status != null) {
            orders = orderRepository.findByOrderDateAndStatus(orderDate, status);
        } else if (orderDate != null) {
            orders = orderRepository.findByOrderDate(orderDate);
        } else if (status != null) {
            orders = orderRepository.findByStatus(status);
        } else {
            orders = orderRepository.findAll();
        }
        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderSummaryResponse getResumeOrdersInfo(LocalDate orderDate, String status) {


        // Assume o status COMPLETE caso não informado
        status = status.isEmpty() ? "COMPLETE" : status;

        List<OrderEntity> orders = orderRepository.findByOrderDateAndStatus(orderDate, status);

        Double totalGeral = orders.stream()
                .mapToDouble(OrderEntity::getOrderAmount)
                .sum();


        List<OrderResumeInfoResponse> orderResumeInfoResponses = orders.stream()
                .map(order -> OrderResumeInfoResponse.builder()
                        .orderId(order.getId())
                        .orderDate(order.getOrderDate().toString())
                        .status(order.getStatus())
                        .customerId(order.getCustomerId())
                        .orderAmount(order.getOrderAmount())
                        .build())
                .collect(Collectors.toList());


        return new OrderSummaryResponse(orderResumeInfoResponses, totalGeral);
    }

    private Double calculateTotal(List<OrderItemRequest> items) {
        return items.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();
    }
}
