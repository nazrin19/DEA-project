package com.example.Lankatools.controller;

import com.example.Lankatools.enums.BookingStatus;
import com.example.Lankatools.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NotificationControllerAdvice {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Globally provides the unread count of pending bookings to all Thymeleaf views.
     * This makes the variable ${unreadCount} accessible everywhere (like inside navbar.html).
     */
    @ModelAttribute("unreadCount")
    public long getUnreadNotificationCount() {
        // Querying the repository directly via the PENDING enum type safely resolves the calculation lookup
        return bookingRepository.countByStatus(BookingStatus.PENDING);
    }
}