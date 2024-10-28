package com.snapstudy.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Document;
import com.snapstudy.backend.repository.DocumentRepository;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    public Page<Document> findDocumentsBySubjectId(Long subjectId, Pageable pageable) {
        return documentRepository.findDocumentsBySubjectId(subjectId, pageable);
    }

    public Document getDocumentById(Long id) {
        Optional<Document> document = documentRepository.findById(id);

        if (document.isPresent()) {
            return document.get();
        } else {
            return null;
        }
    }

    public Document getDocumentByName(String name) {
        Optional<Document> document = documentRepository.findByName(name);

        if (document.isPresent()) {
            return document.get();
        } else {
            return null;
        }
    }

}