package com.snapstudy.backend.model;

import java.util.List;

public class SearchResult {
    private List<Degree> degrees;
    private List<Subject> subjects;
    private List<Document> documents;
    private boolean last;  // Para indicar si hay más páginas

    // Getters y Setters
    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
