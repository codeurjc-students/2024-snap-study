package com.snapstudy.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Subject;
import com.snapstudy.backend.repository.SubjectRepository;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    public Page<Subject> findSubjectsByDegreeId(Long degreeId, Pageable pageable) {
        return subjectRepository.findSubjectsByDegreeId(degreeId, pageable);
    }

    public Subject getSubjectById(Long id) {
        Optional<Subject> Subject = subjectRepository.findById(id);

        if (Subject.isPresent()) {
            return Subject.get();
        } else {
            return null;
        }
    }

}
