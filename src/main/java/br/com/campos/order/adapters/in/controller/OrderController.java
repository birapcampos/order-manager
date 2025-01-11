package br.com.campos.order.adapters.in.controller;

import br.com.campos.order.adapters.in.controller.request.OrderRequest;
import br.com.campos.order.adapters.out.response.OrderResponse;
import br.com.campos.order.application.ports.in.order.CreateOrderInputPort;
import br.com.campos.order.application.ports.in.order.DeleteOrderInputPort;
import br.com.campos.order.application.ports.in.order.GetOrderInputPort;
import br.com.campos.order.application.ports.in.order.UpdateOrderInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    private CreateOrderInputPort createOrderInputPort;
    private UpdateOrderInputPort updateOrderInputPort;
    private GetOrderInputPort getOrderInputPort;
    private DeleteOrderInputPort deleteOrderInputPort;
    public OrderController(CreateOrderInputPort createOrderInputPort,
                           UpdateOrderInputPort updateOrderInputPort,
                           GetOrderInputPort getOrderInputPort,
                           DeleteOrderInputPort deleteOrderInputPort) {
        this.createOrderInputPort = createOrderInputPort;
        this.updateOrderInputPort = updateOrderInputPort;
        this.getOrderInputPort = getOrderInputPort;
        this.deleteOrderInputPort = deleteOrderInputPort;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest order) {
        OrderResponse createdOrder = createOrderInputPort.createOrder(order);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdOrder.getOrderId())
                        .toUri())
                .body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id,
                                                     @RequestBody OrderRequest order) {
        OrderResponse updatedOrder = updateOrderInputPort.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Optional<OrderResponse> orderResponse = getOrderInputPort.getOrderById(id);
        return orderResponse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = getOrderInputPort.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        deleteOrderInputPort.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
