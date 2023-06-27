package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RepositoryRole;
import ru.kata.spring.boot_security.demo.dao.RepositoryUser;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

@Service
public class UserRegistration {

    private final RepositoryUser repositoryUser;
    private final RepositoryRole repositoryRole;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistration(RepositoryUser repositoryUser, UserDao userDao, RepositoryRole repositoryRole, PasswordEncoder passwordEncoder) {
        this.repositoryUser = repositoryUser;
        this.repositoryRole = repositoryRole;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(User user) {
        if (repositoryRole.findAll().isEmpty())
            user.setRoleSet(repositoryRole.save(new Role("ROLE_ADMIN")));
        user.setRoleSet(repositoryRole.save(new Role("ROLE_USER")));
        user.setPass(passwordEncoder.encode(user.getPass()));
        repositoryUser.save(user);
    }
}
