package com.boot.demo1.user.service;

import com.boot.demo1.user.dto.User;
import com.boot.demo1.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(String userName) {
        return this.userRepository.findByUsername(userName);
    }
}
