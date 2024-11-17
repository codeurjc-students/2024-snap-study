package com.snapstudy.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String content; // ELASTICSEARCH/IA RESUME
    @Column(nullable = false)
    private Long repositoryId;
    @Column(nullable = false)
    private String extension;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @JsonBackReference
    private Subject subject;

    public Document() {
    }

    public Document(String name, String content, Subject subject, Long repositoryId, String extension) {
        this.name = name;
        this.content = content;
        this.subject = subject;
        this.repositoryId = repositoryId;
        this.extension = extension;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Long id) {
        this.repositoryId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
