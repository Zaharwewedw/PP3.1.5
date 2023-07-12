package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RepositoryRole;
import ru.kata.spring.boot_security.demo.errors_controles.ErrorResponse;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServerImpl;
import ru.kata.spring.boot_security.demo.service.UserRegistration;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RepositoryRole repositoryRole;
    private final UserRegistration userRegistration;
    private final UserValidator userValidator;
    private final UserService userService;
    private final UserDetailsServerImpl userDetailsServer;

    @Autowired
    public AdminController(RepositoryRole repositoryRole, UserRegistration userRegistration, UserValidator userValidator, UserService userService, UserDetailsServerImpl userDetailsServer) {
        this.repositoryRole = repositoryRole;
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
            StringBuilder errorMessage = new StringBuilder();
            int cnt = 1;
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(cnt).append(") ").append(error.getDefaultMessage()).append("; ");
                cnt++;
            }

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(errorMessage.toString()));
        }

        User existingUser = userService.getByIdUser(user.getId());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        existingUser.setPass(user.getPass());
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());

        Role role = repositoryRole.findByRoleUser(user.getRoleSet().stream().map(Role::getAuthority).findFirst().orElse(""));
        existingUser.getRoleSet().clear();
        existingUser.addRole(role);

        userService.upDateUser(existingUser);

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
                                             BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        User user1 = new User();

        try {
            userDetailsServer.loadUserByUsername(user.getUsername());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Такой пользыватель уже зарегестрирован"));
        } catch (UsernameNotFoundException ignored) {
        }
        try {
            user1.setAge(user.getAge());
            user1.setEmail(user.getEmail());
            user1.setPass(user.getPass());
            user1.setUsername(user.getUsername());
            user1.setName(user.getName());

            Role role = repositoryRole.findByRoleUser(user.getRoleSet().stream().map(Role::getAuthority).findFirst().orElse(""));
            user1.getRoleSet().clear();
            user1.addRole(role);
            userRegistration.register(user1);
            return ResponseEntity.ok("Пользователь успешно создан");

        } catch (ConstraintViolationException e) {

            StringBuilder errorMessage = new StringBuilder();
            int cnt = 1;
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                errorMessage.append(cnt).append(") ").append(violation.getMessage()).append("; ");
                cnt++;
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(errorMessage.toString()));
        }
    }
}