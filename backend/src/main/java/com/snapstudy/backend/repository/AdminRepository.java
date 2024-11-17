package com.snapstudy.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snapstudy.backend.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}
