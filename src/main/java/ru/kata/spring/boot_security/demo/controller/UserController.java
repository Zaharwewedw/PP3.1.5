package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServerImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserDetailsServerImpl userRegistration;
    private final UserServiceImp userServiceImp;
    private long id;
    private final UserDetailsServerImpl userDetailsServer;

    @Autowired
    public UserController(UserDetailsServerImpl userRegistration, UserServiceImp userServiceImp, UserDetailsServerImpl userDetailsServer) {
        this.userRegistration = userRegistration;
        this.userServiceImp = userServiceImp;
        this.userDetailsServer = userDetailsServer;
    }

    @GetMapping("/{id}")
    public String userPages(@PathVariable(name = "id") long id) {
        this.id = id;
        return "users/profile";
    }
    @ResponseBody
    @GetMapping()
    public ResponseEntity<Map<String, User>> userPage(Principal principal) {

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

        Map<String, User> userMap = new HashMap<>();
        userMap.put("principal", userPrincipal);
        userMap.put("user", user);
        return user != null
                ? new ResponseEntity<>(userMap, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
