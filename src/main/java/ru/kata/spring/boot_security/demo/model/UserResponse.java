package ru.kata.spring.boot_security.demo.model;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserResponse {
    private User loggedUser;
    private List <User> usersFromDb;
    private Set <String> roleSet;

    public UserResponse() {
        //Create standard RoleSet
        createStandardRoleSet();
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public List<User> getUsersFromDb() {
        return usersFromDb;
    }

    public void setUsersFromDb(Set <User> usersFromDb) {
        this.usersFromDb = new ArrayList<>();
        this.usersFromDb = usersFromDb.stream()
                .sorted(Comparator.comparing(User :: getId))
                .collect(Collectors.toList());
    }

    public Set<String> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<String> roleSet) {
        this.roleSet = roleSet;
    }

    public void addRolesToSet(Set<Role> roleSet) {
        for (Role role : roleSet) {
            this.roleSet.add(role.getName());
        }
    }

    public void createStandardRoleSet() {
        roleSet = new HashSet<>();
        roleSet.add("ROLE_ADMIN");
        roleSet.add("ROLE_USER");
        roleSet.add("ROLE_ROOT");
        roleSet.add("ROLE_SUPERUSER");
    }

    public void clear() {
        loggedUser = null;
        usersFromDb = null;
        roleSet = null;
        createStandardRoleSet();
    }

    public void clearRoles() {
        roleSet = null;
    }
}
