package com.example.Lankatools.controller;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/{id}/password")
    public String updatePassword(@PathVariable Long id,
                                 @RequestBody Map<String, String> request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String rawPassword = request.get("password");

        // IMPORTANT FIX: encode password
        user.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(user);

        return "Password updated successfully";
    }
}