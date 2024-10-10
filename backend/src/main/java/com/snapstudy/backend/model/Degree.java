package com.snapstudy.backend.model;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

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

    @OneToMany
    @JoinTable(
        name = "degree_subject",
        joinColumns = @JoinColumn(name = "degree_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @JsonBackReference
    private List<Subject> subjects;

    public Degree(){}

    public Degree(String name){
        this.name = name;
        this.postedDate = new Date(System.currentTimeMillis());
        this.subjects = new ArrayList();
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
        this.name = name;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
    
}