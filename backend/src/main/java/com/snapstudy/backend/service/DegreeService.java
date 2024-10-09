package com.snapstudy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.snapstudy.backend.model.Degree;
import com.snapstudy.backend.repository.DegreeRepository;

@Service
public class DegreeService {
  @Autowired
  DegreeRepository degreeRepository;

  public Page<Degree> findDegrees(Pageable pageable) {
    return degreeRepository.findAll(pageable);
  }

  public Degree getDegreeById(Long id) {
    Optional<Degree> degree = degreeRepository.findById(id);

    if (degree.isPresent()) {
      return degree.get();
    } else {
      return null;
    }
  }

}
