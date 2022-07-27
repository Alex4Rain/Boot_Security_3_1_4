package ru.kata.spring.boot_security.demo.model;

import com.sun.istack.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Role> authorities;

    public User (String firstName) {
        this.firstName = firstName;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String firstName, String lastName, String email, String password, List<Role> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(Role role) {
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.add(role);
    }

    public String printAuthoritiesToString() {
        StringBuilder roles = new StringBuilder();
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        for (Role role : authorities) {
            roles.append(role.getName())
                    .append("; ");

        }
        return roles.toString();
    }

    @Override
    public List<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDetails fromUser() {
        return org.springframework.security.core.userdetails.User.builder()
                        .username(this.getEmail())
                        .password(this.getPassword())
                        .authorities(this.getAuthorities())
                        .build();
    }

    public void setAuthoritiesByName(@NotNull String names) {
        List <String> rolesName = Stream.of(names.split("\\s+|,|;"))
                .map(String::trim)
                .collect(Collectors.toList());
        for (String name : rolesName) {
            if (!name.equals("")) {
                this.addAuthority(new Role(name));
            }
        }
    }
}
