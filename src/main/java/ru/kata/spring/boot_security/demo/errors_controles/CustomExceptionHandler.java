package ru.kata.spring.boot_security.demo.errors_controles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;


@RestControllerAdvice
public class CustomExceptionHandler {

    private String info;


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationException> handleUsernameAlreadyExistsException(ValidationException ex) {
        ValidationException errorResponse = new ValidationException();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }


    public String toString() {
        return "Пользователь с таким логином уже существует";
    }
}