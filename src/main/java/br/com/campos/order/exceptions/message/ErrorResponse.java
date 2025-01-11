package br.com.campos.order.exceptions.message;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {

    private String status;
    private String message;
    private LocalDateTime timestamp;
    private List<ErrorDetail> errors;

    public ErrorResponse(String status, String message, LocalDateTime timestamp, List<ErrorDetail> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ErrorDetail> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDetail> errors) {
        this.errors = errors;
    }
}
