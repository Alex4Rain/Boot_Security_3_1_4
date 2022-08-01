package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_roles")
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Transient
    @ManyToMany(mappedBy = "authorities")
    private Set <User> users;

    public Role() {}

    public Role(Long id) {
        this.id = id;
    }

    public Role(String name) {
      this.setName(name);
    }

    public Role(Long id, String name) {
        this.id = id;
        this.setName(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.contains("ROLE_")) {
            this.name = name.toUpperCase();
        } else {
            this.name = "ROLE_" + name.toUpperCase();
        }
    }

    public Set <User> getUsers() {
        return users;
    }

    public void setUsers(Set <User> users) {
        this.users = users;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
