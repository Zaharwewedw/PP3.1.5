package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.Role;

public interface RepositoryRole extends JpaRepository<Role, Long>  {
   Role findByRoleUser(String name);
}
