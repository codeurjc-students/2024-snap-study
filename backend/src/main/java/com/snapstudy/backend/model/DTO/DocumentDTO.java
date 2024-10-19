package com.snapstudy.backend.model.DTO;

import org.springframework.web.multipart.MultipartFile;

public class DocumentDTO {
    private MultipartFile document;
    private Long degree;
    private Long subject;

    public DocumentDTO(){}

    public DocumentDTO(MultipartFile document, Long degree, Long subject){
        this.document = document;
        this.degree = degree;
        this.subject = subject;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }

    public Long getDegree() {
        return degree;
    }

    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

}
