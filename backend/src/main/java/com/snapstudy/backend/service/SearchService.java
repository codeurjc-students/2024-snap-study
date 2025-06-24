package com.snapstudy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        // Search in the Degree table
        Page<Degree> degrees = degreeRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page, size));

        // Search in the Subject table
        Page<Subject> subjects = subjectRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page, size));

        // Search in the Document table
        Page<Document> documents = documentRepository.findByNameContainingIgnoreCase(query, PageRequest.of(page, size));

        return null;
    }
}
