package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl (EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void addUser(User user) {
        if (getUserByEmail(user.getEmail()).getFirstName().contains("notUser")) {
            entityManager.persist(user);
        } else {
            throw new RuntimeException("User with this e-mail already exist in base");
        }
    }

    @Override
    public void removeUser(User user){
        entityManager.remove(entityManager.find(User.class, user.getId()));
    }

    @Override
    public Set <User> setUsers() {
        TypedQuery<User> query = entityManager.createQuery("FROM User", User.class);
        return query.getResultStream().collect(Collectors.toSet());
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUser(Long id) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE id = :id", User.class);
        query.setParameter("id", id);
        return query.getResultList().get(0);
    }
    @Override
    public User getUserByEmail (String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        if (query.getResultList().isEmpty()) {
            return new User("notUser");
        }
        return query.getResultList().get(0);
    }

}
