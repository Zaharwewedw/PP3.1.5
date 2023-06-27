package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RepositoryUser;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImp;

import java.util.Optional;

@Service
public class UserDetailsServerImpl implements UserDetailsService {
    private final RepositoryUser repositoryUser;
    @Autowired
    public UserDetailsServerImpl(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
    }
    public User getUserByUsernameController(String username) {
        Optional<User> user = repositoryUser.getAllByUsNa(username);
        return user.get();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repositoryUser.getAllByUsNa(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return new UserDetailsImp(user.get());
    }
}
