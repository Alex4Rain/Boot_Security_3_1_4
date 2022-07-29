package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;

    }

    @GetMapping()
    public String view(ModelMap model, Principal principal) {
        model.addAttribute("user", service.getUserByEmail(principal.getName()));
        return "user";
    }
}
