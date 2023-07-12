package ru.kata.spring.boot_security.demo.errors_controles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CustomExceptionHandler > handleException(EntityUserNotFoundException exception) {
        CustomExceptionHandler  data = new CustomExceptionHandler ();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomExceptionHandler > handleException(Exception exception) {
        CustomExceptionHandler  data = new CustomExceptionHandler ();
        data.setInfo(exception.getMessage());
        System.out.println(data);

        return new ResponseEntity<>(data , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String errorMessage = "";
        for (ObjectError error : errors) {
            errorMessage += error.getDefaultMessage() + "\n";
        }
        return ResponseEntity.badRequest().body(errorMessage);
    }


}