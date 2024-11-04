package com.snapstudy.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.model.Subject;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Page<Subject> findAll(Pageable page);
    Page<Subject> findSubjectsByDegreeId(Long id, Pageable page);
    Optional<Subject> findById(Long id);
    Optional<Subject> findByNameAndDegree(String name, Degree degree);
}
