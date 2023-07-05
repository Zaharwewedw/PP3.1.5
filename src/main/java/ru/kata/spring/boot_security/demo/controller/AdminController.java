package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServerImpl;
import ru.kata.spring.boot_security.demo.service.UserRegistration;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRegistration userRegistration;
    private final UserValidator userValidator;
    private UserService userService;
    private final UserDetailsServerImpl userDetailsServer;

    @Autowired
    public AdminController(UserRegistration userRegistration, UserValidator userValidator, UserDetailsServerImpl userDetailsServer) {
        this.userRegistration = userRegistration;
        this.userValidator = userValidator;
        this.userDetailsServer = userDetailsServer;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/AllUsers")
    public ModelAndView allUsers(Principal principal) {
        User userPrincipal = userDetailsServer.getUserPrincipalByUsername(principal.getName());
        List<User> users = userService.getAllUsers();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/AllUsers");
        modelAndView.addObject("users", users)
                .addObject("Email", userPrincipal.getEmail())
                .addObject("Roles", userPrincipal.toStringHeader());
        return modelAndView;
    }

    @PutMapping("/update/{id}")
    public String updatePage(@ModelAttribute @Valid User user,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/AllUsers";

        userService.upDateUser(user);

        return "redirect:/admin/AllUsers";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return "redirect:/admin/AllUsers";
    }

    @GetMapping("/registration")
    public String registrationNewUser(@ModelAttribute("user") User user,
                                      Model model,
                                      Principal principal) {

        User userPrincipal = userDetailsServer.getUserPrincipalByUsername(principal.getName());
        model.addAttribute("Email", userPrincipal.getEmail());
        model.addAttribute("Roles", userPrincipal.toStringHeader());

        return "/admin/registration";
    }

    @PostMapping("/registration")
    public String performRegistrationNewUser(@ModelAttribute("user") @Valid User user,
                                             BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "/admin/registration";

        userRegistration.register(user);

        return "redirect:/admin/AllUsers";
    }
}