package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService service;

    @Autowired
    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String index(ModelMap model) {
        model.addAttribute("users", service.listUsers());
        model.addAttribute("user", new User());
        return "admin";
    }

    @PostMapping
    public String addUser(User user, @RequestParam String roleName) {
        user.setAuthoritiesByName(roleName);
        service.addUser(user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/remove")
    public String remove(User user) {
        service.removeUser(user);
        return "redirect:/admin/";
    }
    @GetMapping("/edit/{id}")
    public String edit(ModelMap model, @PathVariable("id") long id) {
        User eUser = service.getUser(id);
        model.addAttribute("userEdit", eUser);
        model.addAttribute("roleEdit", eUser.printAuthoritiesToString());

        return "edit";
    }
    @PatchMapping("/update/{id}")
    public String update(User user, @RequestParam String roleEdit) {
        user.setAuthoritiesByName(roleEdit);
        service.editUser(user);
        return "redirect:/admin/";
    }
    @GetMapping("/user/{id}")
    public String view(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", service.getUser(id));
        return "user";
    }
}
