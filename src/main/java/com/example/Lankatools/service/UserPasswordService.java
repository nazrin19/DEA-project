package com.example.Lankatools.service;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordService {

    private final UserRepository userRepository;

    public UserPasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User updatePassword(Long id, String newPassword) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword); // later you should hash this!

        return userRepository.save(user);
    }
}