package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.enums.BookingStatus; // ◄ 1. Added missing enum import
import com.example.Lankatools.repository.BookingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class NotificationController {

    private final BookingRepository bookingRepository;

    public NotificationController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/notifications")
    public String showNotifications(Model model) {
        // ◄ 2. Fixed: Replaced raw Strings with BookingStatus.PENDING enum
        List<Booking> pendingBookings = bookingRepository.findByStatus(BookingStatus.PENDING);
        long pendingCount = bookingRepository.countByStatus(BookingStatus.PENDING);

        model.addAttribute("bookings", pendingBookings);
        model.addAttribute("notificationCount", pendingCount);

        return "notifications";
    }
}