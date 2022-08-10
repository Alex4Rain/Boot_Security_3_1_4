package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UserResponse;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import java.security.Principal;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rest")
public class AdminController {

    private final UserService service;
    private final RoleService roleService;
    private UserResponse userResponse;

    @Autowired
    public AdminController(UserService service, RoleService roleService, UserResponse userResponse) {
        this.service = service;
        this.roleService = roleService;
        this.userResponse = userResponse;
    }

    @GetMapping
    public UserResponse index(Principal principal) {
        userResponse.clear();
        User loggedUser = service.getUserByEmail(principal.getName());
        userResponse.setLoggedUser(loggedUser);
        if (loggedUser.printAuthoritiesToString().contains("ADMIN")) {
            userResponse.setUsersFromDb(service.setUsers());
            userResponse.addRolesToSet(roleService.setRoles());
        } else {
            userResponse.clearRoles();
        }
        return userResponse;
    }

    @PostMapping
    public UserResponse addUser(@RequestBody Map <String, String> userNew) {
        User newUser = new User(userNew.get("firstName"), userNew.get("lastName"), userNew.get("email"), Integer.parseInt(userNew.get("age")), userNew.get("password"));
        newUser.setAuthoritiesByName(userNew.get("roles"));
        service.addUser(newUser);
        userResponse.setUsersFromDb(service.setUsers());
        return userResponse;
    }

    @DeleteMapping
    public UserResponse remove(@RequestBody Map <String, String> userDel) {
        User deleteUser = service.getUserByEmail(userDel.get("email"));
        if (deleteUser != null) {
            service.removeUser(deleteUser);
        }
        userResponse.setUsersFromDb(service.setUsers());
        return userResponse;
    }

    @PatchMapping
    public UserResponse update(@RequestBody Map <String, String> updatedUser) {
        User upUser = service.getUser(Long.parseLong(updatedUser.get("id")));
        if (upUser != null) {
            upUser.setFirstName(updatedUser.get("firstName"));
            upUser.setLastName(updatedUser.get("lastName"));
            upUser.setAge(Integer.parseInt(updatedUser.get("age")));
            upUser.setEmail(updatedUser.get("email"));
            upUser.setPassword(updatedUser.get("password"));
            upUser.setAuthoritiesByName(updatedUser.get("roles"));
            service.editUser(upUser);
        }
        userResponse.setUsersFromDb(service.setUsers());
        return userResponse;
    }
}
