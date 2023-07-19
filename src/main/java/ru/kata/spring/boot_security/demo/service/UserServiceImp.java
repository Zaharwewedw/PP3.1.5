package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RepositoryRole;
import ru.kata.spring.boot_security.demo.dao.RepositoryUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private final RepositoryUser repositoryUser;
    private final RepositoryRole repositoryRole;

    public UserServiceImp(RepositoryUser repositoryUser, RepositoryRole repositoryRole) {
        this.repositoryUser = repositoryUser;
        this.repositoryRole = repositoryRole;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return repositoryUser.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        repositoryUser.deleteById(id);
    }

    @Override
    @Transactional
    public void upDateUser(User user) {
        User existingUser = getByIdUser(user.getId());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        existingUser.setPass(user.getPass());
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());

        Role role = repositoryRole.findByRoleUser(user.getRoleSet().stream().map(Role::getAuthority).findFirst().orElse(""));
        existingUser.getRoleSet().clear();
        existingUser.addRole(role);
        repositoryUser.save(existingUser);

    }

    @Transactional(readOnly = true)
    @Override
    public User getByIdUser(Long id) {
        return repositoryUser.findById(id).get();
    }

}