package com.snapstudy.backend.model;

import jakarta.persistence.*;

@Entity
public class RepositoryDocument {
/*
 * Para llevar un mejor registro de lo que se guarda lo que hacemos es guardar los repositorios del s3, habrá uno por cada grado y asignatura
 * Software/IPO - Software/DAA - Math/Calculo...
 * Con esto vemos si existe el repositorio en el s3 si en la tabla hay una entrada con el id del grado y de la asignatura, sino se crea
 */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long degreeId;
    @Column(nullable = false)
    private Long subjectId;

    public RepositoryDocument (){}

    public RepositoryDocument (Long degreeId, Long subjectId){
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