package ru.kata.spring.boot_security.demo.errors_controles;

import java.util.Map;

public class ErrorResponse {
    private Map<String, String>  errorMessage;

    public ErrorResponse(Map<String, String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, String>  getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Map<String, String>  errorMessage) {
        this.errorMessage = errorMessage;
    }
}