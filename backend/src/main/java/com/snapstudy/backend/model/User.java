package com.snapstudy.backend.model;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column(length = 1000000)
    @Lob
    private byte[] profilePicture;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, byte[] profile, String... role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setPassword(password);
        this.profilePicture = profile;
        this.roles = List.of(role);
    }

    public User(String firstName, String lastName, String email, String password, String... role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setPassword(password);
        this.profilePicture = null;
        this.roles = List.of(role);
    }

    public User(User t) {
        this.firstName = t.firstName;
        this.lastName = t.lastName;
        this.email = t.email;
        this.password = "";
        this.profilePicture = t.profilePicture;
        this.roles = t.roles;
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

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
