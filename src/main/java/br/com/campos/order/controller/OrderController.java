package br.com.campos.order.controller;

import br.com.campos.order.controller.dto.request.OrderRequest;
import br.com.campos.order.controller.dto.response.OrderResponse;
import br.com.campos.order.controller.dto.response.OrderSummaryResponse;
import br.com.campos.order.service.OrderService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Cria um novo pedido", description = "Endpoint para criar um pedido de vendas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest orderRequest) {

        OrderResponse createdOrder = orderService.createOrder(orderRequest);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(createdOrder.getOrderId())
                        .toUri())
                .body(createdOrder);
    }

    @Operation(summary = "Lista todos os pedidos", description = "Endpoint para listar todos os pedidos, com filtros de data e status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam(required = true) String orderDate,
                                                            @RequestParam(required = false) String status) {


        LocalDate parsedDate = (orderDate != null) ? LocalDate.parse(orderDate) : null;
        List<OrderResponse> orders = orderService.getAllOrders(parsedDate, status);
        return ResponseEntity.ok(orders);

    }

    @Operation(summary = "Obtém o resumo dos pedidos", description = "Endpoint para obter um resumo dos pedidos com base em data e status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumo dos pedidos encontrado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/summary")
    public ResponseEntity<OrderSummaryResponse> getOrderSummary(@RequestParam(required = true) String orderDate,
                                                                @RequestParam(required = false) String status) {

        LocalDate parsedDate = (orderDate != null) ? LocalDate.parse(orderDate) : null;
        OrderSummaryResponse orderSummary = orderService.getResumeOrdersInfo(parsedDate, status);
        return ResponseEntity.ok(orderSummary);
    }

    @Operation(summary = "Obtém um pedido específico", description = "Endpoint para obter os detalhes de um pedido pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId){
        Optional<OrderResponse> orderResponse = orderService.getOrder(orderId);

        return orderResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
