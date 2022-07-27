package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Transactional
    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.addUser(user);
    }

    @Transactional
    @Override
    public void removeUser(User user){
        userDao.removeUser(user);
    }

    @Transactional
    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Transactional
    @Override
    public void editUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.editUser(user);
    }

    @Transactional
    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Transactional
    @Override
    public User getUserByEmail (String email) {
        return userDao.getUserByEmail(email);
    }

    @Transactional
    @Override
    public void addRole(Role role) {roleDao.addRole(role);}

    @Transactional
    @Override
    public void removeRole(Role role) {roleDao.removeRole(role);}

    @Transactional
    @Override
    public List<Role> listRoles() {return roleDao.listRoles();}

    @Transactional
    @Override
    public Role getRole(Long id) {return roleDao.getRole(id);}

}
