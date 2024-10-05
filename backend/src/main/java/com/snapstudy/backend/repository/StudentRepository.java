package com.snapstudy.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snapstudy.backend.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
    Optional<Student> findByEmail(String email);
}
