package com.snapstudy.backend.model;

import jakarta.persistence.*;

@Entity
public class RepositoryDocument {
    /*
     * To keep a better record of what is stored, we save
     * the S3 repositories. There will be one for each grade and subject,
     * such as Software/IPO, Software/DAA, Math/Calculus...
     * This way, we check if the repository exists in S3 by verifying
     * if there is an entry in the table with the grade ID and subject ID.
     * If not, it is created.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long degreeId;
    @Column(nullable = false)
    private Long subjectId;

    public RepositoryDocument() {
    }

    public RepositoryDocument(Long degreeId, Long subjectId) {
        this.degreeId = degreeId;
        this.subjectId = subjectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}