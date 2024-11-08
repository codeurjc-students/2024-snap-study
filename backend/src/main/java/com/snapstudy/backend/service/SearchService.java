package com.snapstudy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.model.SearchResult;
import com.snapstudy.backend.model.Subject;
import com.snapstudy.backend.repository.DegreeRepository;
import com.snapstudy.backend.repository.DocumentRepository;
import com.snapstudy.backend.repository.SubjectRepository;

@Service
public class SearchService {

    @Autowired
    private DegreeRepository degreeRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private DocumentRepository documentRepository;

    public SearchResult search(String query, int page, int size) {
        // Búsqueda en la tabla Degree
        Page<Degree> degrees = degreeRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page, size));

        // Búsqueda en la tabla Subject
        Page<Subject> subjects = subjectRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page, size));

        // Búsqueda en la tabla Document
        Page<Document> documents = documentRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page, size));

        // Crear el resultado con los resultados de las 3 tablas
        SearchResult searchResult = new SearchResult();
        searchResult.setDegrees(degrees.getContent());
        searchResult.setSubjects(subjects.getContent());
        searchResult.setDocuments(documents.getContent());
        searchResult.setLast(degrees.isLast() && subjects.isLast() && documents.isLast()); // Verifica si hay más resultados

        return searchResult;
    }
}
