package com.example.Lankatools.controller;

import com.example.Lankatools.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Listens for form submissions from the frontend UI
     */
    @PostMapping("/create")
    public String handleNewBooking(
            @RequestParam("customerEmail") String customerEmail,
            @RequestParam("toolName") String toolName,
            @RequestParam("bookingDate") String bookingDate) {

        // Triggers the booking logic and automatic email
        bookingService.createBooking(customerEmail, toolName, bookingDate);

        // Redirects back to your booking view with a success flag
        return "redirect:/bookings?success=true";
    }
}