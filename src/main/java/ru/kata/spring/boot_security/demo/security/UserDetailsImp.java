package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.dao.RepositoryUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;


public class UserDetailsImp implements UserDetails {
    private final User user;
    private final RepositoryUser repositoryUser;

    public UserDetailsImp(User user, RepositoryUser repositoryUser) {
        this.user = user;
        this.repositoryUser = repositoryUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Optional<User> optionalUser = repositoryUser.findByUsernameWithRoles(user.getUsername());
        Set<Role> roles = optionalUser.map(User::getRoleSet).orElse(new HashSet<>());
        return new ArrayList<>(roles);
    }

    @Override
    public String getPassword() {
        return this.user.getPass();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getPerson() {
        return this.user;
    }
}

