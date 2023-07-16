package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServerImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDetailsServerImpl userRegistration;
    private final UserServiceImp userServiceImp;

    private final UserDetailsServerImpl userDetailsServer;

    @Autowired
    public UserController(UserDetailsServerImpl userRegistration, UserServiceImp userServiceImp, UserDetailsServerImpl userDetailsServer) {
        this.userRegistration = userRegistration;
        this.userServiceImp = userServiceImp;
        this.userDetailsServer = userDetailsServer;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Map<String,User>>> userPage(Principal principal, @PathVariable(name = "id") long id) {

        String username = principal.getName();
        User currentUser = userRegistration.getUserByUsernameController(username);

        if (id == 0) {
            id = currentUser.getId();
        }

        for (Role role : currentUser.getRoleSet()) {

            if (role.getAuthority().equals("ROLE_ADMIN")) {
                break;
            }
            if (role.getAuthority().equals("ROLE_USER")) {
                if (!Objects.equals(currentUser.getId(), id)) {
                    throw new AccessDeniedException("Доступ запрещен");
                }
            }
        }
        User userPrincipal = userDetailsServer.getUserPrincipalByUsername(principal.getName());
        User user = userServiceImp.getByIdUser(id);
        List<Map<String,User>> restUserJson = new ArrayList<>();
        Map <String, User> userMap = new HashMap<>();
        userMap.put("principal", userPrincipal);
        userMap.put("user", user);
        restUserJson.add(userMap);
        return new ResponseEntity<>(restUserJson, HttpStatus.OK);
    }
}
