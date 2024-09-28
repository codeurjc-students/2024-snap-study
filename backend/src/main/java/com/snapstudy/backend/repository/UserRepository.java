package com.snapstudy.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snapstudy.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}

