package com.snapstudy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Admin;
import com.snapstudy.backend.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Student not found with name " + email));
    }
    
}
