package com.example.Lankatools.service;

import com.example.Lankatools.dto.LoginRequest;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Role;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setApproved(false);
        return userRepository.save(user);
    }
    public List<User> getAllUsers(Role role) {
        if (role != null) {
            return userRepository.findAll().stream().filter(user -> user.getRole() == role).toList();
        }
        return userRepository.findAll();
    }

    public User toggleUserSuspension(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setActive(!user.getActive());
            return userRepository.save(user);
        }).orElse(null);
    }

    public User approveUser(Long id){
        return userRepository.findById(id).map(user -> {
            user.setApproved(true);
            return userRepository.save(user);
        }).orElse(null);
    }

    /**
     * 🟢 ADD THIS METHOD: Finds a user by their email address
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public User login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + loginRequest.getEmail()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

}