package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    @Autowired
    public AuthController(UserService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, @RequestParam String roleName) {
        user.setAuthoritiesByName(roleName);
        service.addUser(user);
        return "redirect:/auth/login";
    }

}
