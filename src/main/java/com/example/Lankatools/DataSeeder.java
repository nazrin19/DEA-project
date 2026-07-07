package com.example.Lankatools;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Role;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder; // Remove this line if you don't use Spring Security
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Remove this if you don't use a password encoder

    // Constructor injection for both beans
    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Remove this if you don't use a password encoder
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> existingAdmin = userRepository.findByEmail("admin@dea.com");

        if (existingAdmin.isEmpty()) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@dea.com")
                    // .password("admin123") -> Use this line instead if you DO NOT use password encoder
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .approved(true)
                    .build();

            userRepository.save(admin);
            System.out.println("==============================================");
            System.out.println("👉 SEEDED ADMIN ACCOUNT: admin@dea.com / admin123");
            System.out.println("==============================================");
        }
    }
}