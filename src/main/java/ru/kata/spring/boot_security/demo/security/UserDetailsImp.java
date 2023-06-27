package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsImp implements UserDetails {
    private final User user;

    public UserDetailsImp(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = user.getRoleSet();
        String roleUser = "ROLE_USER";
        for (Role role : roles) {
            if (role.getAuthority().equals("ROLE_ADMIN")) {
                return Collections.singletonList(new SimpleGrantedAuthority(role.getAuthority()));
            }
        }
        return Collections.singletonList(new SimpleGrantedAuthority(roleUser));
    }

    @Override
    public String getPassword() {
        return this.user.getPass();
    }

    @Override
    public String getUsername() {
        return this.user.getUsNa();
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

