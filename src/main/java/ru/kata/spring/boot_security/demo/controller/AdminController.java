package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServerImpl;
import ru.kata.spring.boot_security.demo.service.UserRegistration;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController { ;
    private final UserRegistration userRegistration;
    private final UserValidator userValidator;
    private final UserService userService;
    private final UserDetailsServerImpl userDetailsServer;

    @Autowired
    public AdminController(UserRegistration userRegistration,
                           UserValidator userValidator,
                           UserService userService,
                           UserDetailsServerImpl userDetailsServer) {
        this.userRegistration = userRegistration;
        this.userValidator = userValidator;
        this.userService = userService;
        this.userDetailsServer = userDetailsServer;
    }
    @GetMapping(value = "/AllUsers")
    public String allUsers() {
        return "admin/AllUsers";
    }

    @ResponseBody
    @GetMapping(value = "/AllUsersRest")
    public ResponseEntity<List<User>> allUsers(Principal principal) {
        User userPrincipal = userDetailsServer.getUserPrincipalByUsername(principal.getName());
        List<User> users = userService.getAllUsers();
        users.add(userPrincipal);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @ResponseBody
    @PutMapping("/update")
    public ResponseEntity<?> updatePage(@Valid @RequestBody User user, BindingResult result) {

        userValidator.validate(user, result);

        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(err -> err instanceof FieldError ? ((FieldError) err).getField() + ":"
                                    + err.getDefaultMessage() : err.getDefaultMessage()).collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        userService.upDateUser(user);
        return ResponseEntity.ok("Пользователь успешно обновлен");
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Пользователь успешно удален");
    }

    @GetMapping("/registration")
    public String registrationNewUser(){
        return "/admin/registration";
    }

    @ResponseBody
    @GetMapping("/registrationRest")
    public ResponseEntity<User> registrationNewUser(Principal principal) {
        User userPrincipal = userDetailsServer.getUserPrincipalByUsername(principal.getName());
        return new ResponseEntity<>(userPrincipal, HttpStatus.OK);
    }



    @PostMapping("/registration")
    public ResponseEntity<?> performRegistrationNewUser(@Valid @RequestBody User user,
                                                        BindingResult result) {
        userValidator.validate(user, result);

        try {
            userDetailsServer.getUserByUsernameController(user.getUsername());
            return ResponseEntity.badRequest().body(new
                    ArrayList<>(Collections.singleton("username:такой пользыватель зарегестрирован")));
        } catch (Exception ignoring) {}

        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(err -> err instanceof FieldError ? ((FieldError) err).getField() + ":"
                            + err.getDefaultMessage() : err.getDefaultMessage()).collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        userRegistration.register(user);
        return ResponseEntity.ok("Пользователь успешно обновлен");
    }
}