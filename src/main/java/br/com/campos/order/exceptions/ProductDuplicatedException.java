package br.com.campos.order.exceptions;

public class ProductDuplicatedException extends RuntimeException{
    public ProductDuplicatedException(String message) {
        super(message);
    }
}
