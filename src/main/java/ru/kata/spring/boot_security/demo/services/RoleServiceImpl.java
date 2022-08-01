package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import javax.transaction.Transactional;
import java.util.Set;

@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
    @Override
    public void addRole(Role role) {roleDao.addRole(role);}

    @Override
    public void removeRole(Role role) {roleDao.removeRole(role);}

    @Override
    public Set <Role> setRoles() {return roleDao.setRoles();}

    @Override
    public Role getRole(Long id) {return roleDao.getRole(id);}

}
