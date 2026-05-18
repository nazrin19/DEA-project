package com.example.Lankatools.service;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void suspend(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    public void approveShopOwner(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + id));
        user.setIsApproved(true);
        userRepository.save(user);
    }

}