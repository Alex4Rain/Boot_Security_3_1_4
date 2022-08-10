package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.Set;

public interface UserDao {
    void addUser(User user);
    void removeUser(User user);
    Set <User> setUsers();
    void editUser(User user);
    User getUser(Long id);
    User getUserByEmail (String email);
}
