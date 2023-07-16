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
        System.out.println(user.getName() + " " + user.getPass() + " " + user.getUsername()
                + " " + user.getRoleSet() + " " + user.getAge()+ " " + user.getEmail());
        Role role = null;
        if (!user.getRoleSet().isEmpty()) {
            String authority = user.getRoleSet().stream().map(Role::getAuthority).findFirst().orElse("");
            role = repositoryRole.findByRoleUser(authority);
        }

        if (role == null) {
            role = new Role("ROLE_USER"); // Создаем новый объект "Role"
            repositoryRole.save(role); // Сохраняем его в базе данных
        }

        user.addRole(role);
        user.setPass(passwordEncoder.encode(user.getPass()));
        repositoryUser.save(user);
    }
}