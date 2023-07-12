package ru.kata.spring.boot_security.demo.errors_controles;

public class EntityUserNotFoundException extends RuntimeException{
    public EntityUserNotFoundException(String message) {
        super(message);
    }
}