package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService service;
    private final RoleService roleService;
    private final Set<Role> defaultRoles;

    @Autowired
    public AdminController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
        defaultRoles = new HashSet<>();
        defaultRoles.add(new Role("user"));
        defaultRoles.add(new Role("admin"));
        defaultRoles.add(new Role("root"));
        defaultRoles.add(new Role("superuser"));
        defaultRoles.addAll(roleService.setRoles());
    }

    @GetMapping
    public String index(ModelMap model, Principal user) {
        model.addAttribute("allUsers", service.setUsers());
        model.addAttribute("newUser", new User());
        model.addAttribute("thisUser", service.getUserByEmail(user.getName()));
        model.addAttribute("rolesSet", defaultRoles);
        return "admin_new";
    }

    @PostMapping("/add")
    public String addUser(User user, @RequestParam(value = "roles") String roles) {
        user.setAuthoritiesByName(roles);
        service.addUser(user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/remove")
    public String remove(User user) {
        service.removeUser(user);
        return "redirect:/admin/";
    }

    @PatchMapping("/edit")
    public String update(@ModelAttribute("user") User user, @RequestParam(value = "editRoles") String editRoles) {
        user.setAuthoritiesByName(editRoles);
        service.editUser(user);
        return "redirect:/admin/";
    }
    @GetMapping("/user/{id}")
    public String view(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", service.getUser(id));
        return "user_new";
    }
}
