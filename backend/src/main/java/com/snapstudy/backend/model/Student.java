package com.snapstudy.backend.model;

import jakarta.persistence.Entity;

@Entity
public class Student extends User {
    // Constructors
    public Student() {
    }

    public Student(String firstName, String lastName, String email, String password, byte[] profile) {
        super(firstName, lastName, email, password, profile, "STUDENT");
    }

    public Student(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, "STUDENT");
    }
}
