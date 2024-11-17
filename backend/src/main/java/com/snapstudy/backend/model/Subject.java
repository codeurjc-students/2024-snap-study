package com.snapstudy.backend.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Date postedDate;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Document> documents;

    @ManyToOne
    @JoinColumn(name = "degree_id", nullable = false)
    @JsonBackReference
    private Degree degree;

    public Subject() {
    }

    public Subject(String name, Degree degree) {
        this.name = name;
        this.postedDate = new Date(System.currentTimeMillis());
        this.degree = degree;
        this.documents = new ArrayList();
    }

    public Long getId() {
        return id;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

}
