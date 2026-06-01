package com.example.Lankatools.entity;

import com.example.Lankatools.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Enter a valid email")
    @Column(unique = true, nullable = false, length=255)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String shopName;
    private String shopAddress;
    private String phone;

    // Keeps DB column as 'is_approved' but fixes Lombok method name to getApproved()
    @Column(name = "is_approved", nullable = false)
    @Builder.Default
    private Boolean approved = false;

    // Keeps DB column as 'is_active' but fixes Lombok method name to getActive()
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Custom constructor safely matching the new fields
    public User(String name, String password, Role role, String email, String phone, String shopName, String shopAddress, Boolean active, Boolean approved, LocalDateTime createdAt) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.active = active;
        this.approved = approved;
        this.createdAt = createdAt;
    }
}