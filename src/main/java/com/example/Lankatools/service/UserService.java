package com.example.Lankatools.service;

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

    /**
     * Registers a new user with secure password hashing.
     * Dynamically assigns appropriate platform access levels based on input parameters.
     */
    public User registerUser(User user) {
        // 1. Hash the raw incoming text password string exactly once
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 2. Base platform accessibility state profiles
        user.setActive(true);

        // 3. Evaluate conditional routing paths based on Tool Provider criteria
        if (user.getShopName() != null && !user.getShopName().trim().isEmpty()) {
            user.setRole(Role.SHOP_OWNER);
            user.setApproved(false); // Vendor shops require standard admin review
        } else {
            user.setRole(Role.CUSTOMER);
            user.setApproved(true);  // Consumer client profiles are auto-approved immediately
        }

        return userRepository.save(user);
    }

    /**
     * Filters and collects users from persistent storage based on application access rules.
     */
    public List<User> getAllUsers(Role role) {
        if (role != null) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getRole() == role)
                    .toList();
        }
        return userRepository.findAll();
    }

    /**
     * Toggles account accessibility status to handle policy violations or platform suspensions.
     */
    public User toggleUserSuspension(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setActive(!user.getActive());
            return userRepository.save(user);
        }).orElse(null);
    }

    /**
     * Authorizes pending tool vendor storefront profiles for operational hosting clearance.
     */
    public User approveUser(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setApproved(true);
            return userRepository.save(user);
        }).orElse(null);
    }

    /**
     * Resolves and extracts complete user information matching a unique identification email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
    public void approveShopOwner(Long id) {
        this.approveUser(id);
    }

    public void suspend(Long id) {
        this.toggleUserSuspension(id);
    }
}