package com.example.Lankatools.entity;

import com.example.Lankatools.enums.Toolstatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "tools")
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tool name is required")
    private String name;

    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @Positive(message = "Daily rate must be greater than zero")
    private double dailyRate;

    // FIXED: Added explicit mapping to match the database snake_case column
    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Toolstatus status = Toolstatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Toolstatus getStatus() {
        return status;
    }

    public User getOwner() {
        return owner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(Toolstatus status) {
        this.status = status;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}