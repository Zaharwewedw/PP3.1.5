package ru.kata.spring.boot_security.demo.errors_controles;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EntityUserErrorResponse handleValidationException(ValidationException ex) {

        List<String> errorMessages = ex.getErrorMessages();

        EntityUserErrorResponse response = new EntityUserErrorResponse();
        response.setErrorMessages(errorMessages);

        return response;
    }
}