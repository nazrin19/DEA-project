package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.repository.BookingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class NotificationController {

    private final BookingRepository bookingRepository;

    // Constructor injection pulls in your fresh repository methods safely
    public NotificationController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/notifications")
    public String showNotifications(Model model) {
        // Fetch values using the repository queries you just added
        List<Booking> pendingBookings = bookingRepository.findByStatus("PENDING");
        long pendingCount = bookingRepository.countByStatus("PENDING");

        // Pass data arrays to the Thymeleaf web template layout
        model.addAttribute("bookings", pendingBookings);
        model.addAttribute("notificationCount", pendingCount);

        return "notifications"; // Points to templates/notifications.html
    }
}
