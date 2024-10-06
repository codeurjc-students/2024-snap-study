package com.snapstudy.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToMany
    @JoinColumn(name = "subject_id")
    @JsonBackReference
    private List<Document> documents;

    @ManyToMany(mappedBy = "subjects")  // La relación inversa
    @JsonManagedReference
    private List<Degree> degrees;
}
