package ru.kata.spring.boot_security.demo.model;

import com.sun.istack.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "age")
    private int age;

    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Set <Role> authorities;

    public User (String firstName) {
        this.firstName = firstName;
    }

    public User(String firstName, String lastName, String email, int age, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String firstName, String lastName, String email, int age, String password, Set <Role> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public void setAuthorities(Set <Role> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(Role role) {
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        authorities.add(role);
    }

    public String printAuthoritiesToString() {
        StringBuilder roles = new StringBuilder();
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        for (Role role : authorities) {
            roles.append(role.getName().replaceAll("ROLE_",""))
                    .append("; ");

        }
        roles.setLength(roles.length() - 2);
        return roles.toString();
    }

    @Override
    public Set <Role> getAuthorities() {
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
        Set <String> rolesName = Stream.of(names.split("\\s+|,|;"))
                .map(String::trim)
                .collect(Collectors.toSet());
        for (String name : rolesName) {
            if (!name.equals("")) {
                this.addAuthority(new Role(name));
            }
        }
    }

    class userComparator implements Comparator <User> {
        @Override
        public int compare(User u1, User u2) {
            return u1.getId().compareTo(u2.getId());
        }

    }
}
