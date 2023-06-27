package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserRegistration;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRegistration userRegistration;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(UserRegistration userRegistration, UserValidator userValidator) {
        this.userRegistration = userRegistration;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String LoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationNewUser(@ModelAttribute("user") User user) {

        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistrationNewUser(@ModelAttribute("user") @Valid User user,
                                             BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        userRegistration.register(user);

        return "redirect:/auth/login";
    }
}
