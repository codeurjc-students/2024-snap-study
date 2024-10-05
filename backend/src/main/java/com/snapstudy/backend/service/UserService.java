package com.snapstudy.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapstudy.backend.model.User;
import com.snapstudy.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void setUser(User user){
        userRepository.save(user);
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getById(Long userId){
        return userRepository.findById(userId);
    }
}
