package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.Set;

public interface RoleService {
    void addRole(Role role);
    void removeRole(Role role);
    Set <Role> setRoles();
    Role getRole(Long id);
}
