package com.snapstudy.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.snapstudy.backend.model.Degree;

public interface DegreeRepository extends JpaRepository<Degree, Long> {
    Page<Degree> findAll(Pageable page);
    
}
