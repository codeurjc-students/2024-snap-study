package com.snapstudy.backend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.User;
import com.snapstudy.backend.repository.UserRepository;

@Service
public class TestDataInitializer {

        @Autowired
        private UserRepository userRepository;

        @PostConstruct
        public void init() throws Exception {
            System.out.println("-------------------------------------------");
            System.out.println("-------------------------------------------");
            System.out.println("-------------------------------------------");
            User user1 = new User("Javier", "Salas", "javiisalaas97@gmail.com", "hola", "STUDENT");
            userRepository.save(user1);
        }
}
