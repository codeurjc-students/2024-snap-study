package com.snapstudy.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.snapstudy.backend.model.Document;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findAll(Pageable page);
    Page<Document> findDocumentsBySubjectId(Long id, Pageable page);
    Optional<Document> findById(Long id);
    Optional<Document> findByName(String name);
}