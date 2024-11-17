package com.snapstudy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snapstudy.backend.model.RepositoryDocument;

import java.util.Optional;
import java.util.List;

public interface RepositoryDocumentsRepository extends JpaRepository<RepositoryDocument, Long> {
    Optional<RepositoryDocument> findById(Long id);

    @Query("SELECT r FROM RepositoryDocument r WHERE r.degreeId = :degreeId AND r.subjectId = :subjectId")
    Optional<RepositoryDocument> findByDegreeIdAndSubjectId(@Param("degreeId") Long degreeId,
            @Param("subjectId") Long subjectId);

    @Query("SELECT r FROM RepositoryDocument r WHERE r.degreeId = :degreeId")
    List<RepositoryDocument> findByDegreeId(Long degreeId);
}
