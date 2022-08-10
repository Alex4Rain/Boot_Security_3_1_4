package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.Set;

public interface RoleDao {
    void addRole(Role role);
    void removeRole(Role role);
    Set<Role> setRoles();
    Role getRole(Long id);
}
