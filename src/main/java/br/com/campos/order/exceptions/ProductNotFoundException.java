package br.com.campos.order.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {

        super("Produto não encontrado com este Id: " + id);
    }
}
