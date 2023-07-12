package ru.kata.spring.boot_security.demo.errors_controles;

import java.util.List;

public class ValidationException extends Exception {
    private List<String> errorMessages;
    private String info;

    public ValidationException() {
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
    @Override
    public String toString() {
        return "EntityUserErrorResponse{" +
                "errorMessages=" + errorMessages +
                ", info='" + info + '\'' +
        '}';
    }
}
