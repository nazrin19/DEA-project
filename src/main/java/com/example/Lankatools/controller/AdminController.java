package com.example.Lankatools.controller;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Role;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) Role role) {
        if (role != null) {
            List<User> filteredUsers = userRepository.findAll().stream()
                    .filter(user -> user.getRole() == role)
                    .toList();
            return ResponseEntity.ok(filteredUsers);
        }
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<User> suspendUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            // Uses your defined getActive() and setActive() methods
            user.setActive(!user.getActive());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/approve")
    public ResponseEntity<User> approveUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            // Uses your defined setApproved() method
            user.setApproved(true);
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }
}
