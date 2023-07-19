package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RepositoryRole;
import ru.kata.spring.boot_security.demo.dao.RepositoryUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;

@Service
public class UserRegistration {

    private final RepositoryUser repositoryUser;
    private final RepositoryRole repositoryRole;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistration(RepositoryUser repositoryUser, RepositoryRole repositoryRole, PasswordEncoder passwordEncoder) {
        this.repositoryUser = repositoryUser;
        this.repositoryRole = repositoryRole;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostConstruct
    public void registerDefault() {
        if (repositoryUser.findAll().isEmpty()) {
            User user = new User();
            user.setAge(22);
            user.setPass("akademy");
            user.setUsername("akademy");
            user.setEmail("kata@gmail.com");
            user.setName("Person");
            user.addRole(repositoryRole.save(new Role("ROLE_ADMIN")));
            user.addRole(repositoryRole.save(new Role("ROLE_USER")));
            user.setPass(passwordEncoder.encode(user.getPass()));
            repositoryUser.save(user);
        }
    }

    @Transactional
    public void register(User user) {

        User user1 = new User();
        user1.setAge(user.getAge());
        user1.setEmail(user.getEmail());
        user1.setPass(user.getPass());
        user1.setUsername(user.getUsername());
        user1.setName(user.getName());

        Role role = repositoryRole.findByRoleUser(user.getRoleSet().stream().map(Role::getAuthority).findFirst().orElse(""));
        user1.getRoleSet().clear();
        user1.addRole(role);


        if (role == null) {
            role = new Role("ROLE_USER");
            repositoryRole.save(role);
        }

        user1.addRole(role);
        user1.setPass(passwordEncoder.encode(user1.getPass()));
        repositoryUser.save(user1);

    }
}