package ru.kata.spring.boot_security.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RepositoryUser;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.RollbackException;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private final RepositoryUser repositoryUser;

    public UserServiceImp(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
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
        repositoryUser.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getByIdUser(Long id) {
        return repositoryUser.findById(id).get();
    }

}