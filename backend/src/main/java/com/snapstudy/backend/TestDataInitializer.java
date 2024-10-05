package com.snapstudy.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.Student;
import com.snapstudy.backend.repository.StudentRepository;

@Service
public class TestDataInitializer {

        @Autowired
        private StudentRepository studenRepository;

        @PostConstruct
        public void init() throws Exception {
            Student user1 = new Student("Javier", "Salas", "javiisalaas97@gmail.com", "hola");
            studenRepository.save(user1);
        }
}
