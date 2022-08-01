package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public RoleDaoImpl (EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }
    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void removeRole(Role role) {
        entityManager.remove(entityManager.find(Role.class, role.getId()));
    }

    @Override
    public Set <Role> setRoles() {
        TypedQuery<Role> query = entityManager.createQuery("FROM Role", Role.class);
        return query.getResultStream().collect(Collectors.toSet());
    }
    @Override
    public Role getRole(Long id) {
        TypedQuery<Role> query = entityManager.createQuery("FROM Role WHERE id = :id", Role.class);
        query.setParameter("id", id);
        return query.getResultList().get(0);
    }

}
