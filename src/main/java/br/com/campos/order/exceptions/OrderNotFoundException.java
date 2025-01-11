package br.com.campos.order.exceptions;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String id) {
        super("Pedido de vendas não encontrado com o id: " + id);
    }
}
