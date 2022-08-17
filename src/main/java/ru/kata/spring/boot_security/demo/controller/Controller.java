package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class Controller {

    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public Controller(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @GetMapping("/currentUser")
    public User getCurrentUser(Principal principal) {
        return service.getUserByEmail(principal.getName());
    }

    @GetMapping("/users")
    public List <User> getAllUsersWithAuth(Principal principal) {
        if (getCurrentUser(principal).printAuthoritiesToString().contains("ADMIN")) {
            return getAllUsers();
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/roles")
    public List <Role> getRoles(Principal principal) {
        if (getCurrentUser(principal).printAuthoritiesToString().contains("ADMIN")) {
            return roleService.setRoles().stream()
                    .sorted(Comparator.comparing(Role:: getName))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @PostMapping("/admin/user")
    public List <User> addUser(@RequestBody User newUser) {
        if (newUser.getAuthorities().isEmpty()) {
            newUser.setAuthoritiesByName("USER");
        }
        service.addUser(newUser);
        return getAllUsers();
    }

    @DeleteMapping("/admin/user")
    public List <User> remove(@RequestBody User deletedUser) {
        service.removeUser(deletedUser);
        return getAllUsers();
    }

    @PatchMapping("/admin/user")
    public List <User> update(@RequestBody User updatedUser) {
        if (updatedUser.getAuthorities().isEmpty()) {
            updatedUser.setAuthoritiesByName("USER");
        }
            service.editUser(updatedUser);
        return getAllUsers();
    }

    public List <User> getAllUsers() {
        return service.setUsers().stream()
                .sorted(Comparator.comparing(User :: getId))
                .collect(Collectors.toList());
    }
}
