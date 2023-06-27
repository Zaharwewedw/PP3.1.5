package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private UserDao userDaoImp;

    @Autowired
    public void setUserDAO(UserDao userDaoImp) {
        this.userDaoImp = userDaoImp;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDaoImp.getAllUsers();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        userDaoImp.saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDaoImp.deleteUser(id);
    }

    @Override
    @Transactional
    public void upDateUser(User user) {
        userDaoImp.upDateUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User getByIdUser(Long id) {
        return userDaoImp.getByIdUser(id);
    }


}