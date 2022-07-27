package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    void removeUser(User user);
    List<User> listUsers();
    void editUser(User user);
    User getUser(Long id);
    User getUserByEmail (String email);
    void addRole(Role role);
    void removeRole(Role role);
    List<Role> listRoles();
    Role getRole(Long id);
}
