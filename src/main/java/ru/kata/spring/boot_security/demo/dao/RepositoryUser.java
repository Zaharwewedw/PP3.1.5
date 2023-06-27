package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

public interface RepositoryUser extends JpaRepository<User, Long> {
    Optional<User> getAllByUsNa(String username);
}
