package com.snapstudy.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String content; //para ELASTICSEARCH/IA RESUMEN

    @ManyToOne
    @JoinColumn(name = "subject_id")  // Clave foránea que hace referencia a Subject
    @JsonManagedReference
    private Subject subject;
}
