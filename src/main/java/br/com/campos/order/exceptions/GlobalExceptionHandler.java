package br.com.campos.order.exceptions;

import br.com.campos.order.exceptions.message.ErrorDetail;
import br.com.campos.order.exceptions.message.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail(
                        error.getField(),
                        String.format("%s", error.getDefaultMessage())))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                "Requisição inválida",
                "Existem erros nos campos enviados. Verifique e tente novamente.",
                LocalDateTime.now(),
                errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Produto não encontrado",
                ex.getMessage(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Pedido não encontrado",
                ex.getMessage(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleProductDuplicatedException(ProductDuplicatedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Produto duplicado",
                ex.getMessage(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Erro interno no servidor",
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.",
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Requisição inválida",
                ex.getMessage(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}


