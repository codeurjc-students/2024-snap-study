package com.snapstudy.backend.model;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Date postedDate;

    @ManyToMany
    @JoinTable(
        name = "degree_subject",
        joinColumns = @JoinColumn(name = "degree_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @JsonBackReference
    private List<Subject> subjects;
}