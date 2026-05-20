package com.example.Lankatools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    // 1. Inject your new EmailService
    @Autowired
    private EmailService emailService;

    /**
     * Handles creating a new tool booking and sending an email alert
     */
    public void createBooking(String customerEmail, String toolName, String bookingDate) {

        // TODO: In the next assignment step, you will add repository.save() here to save to MySQL!
        System.out.println("Booking saved in system for tool: " + toolName);

        // 2. Automatically build and trigger the email message
        String subject = "🛠️ Lankatools - Booking Confirmation!";
        String body = "Dear Customer,\n\n" +
                "Your booking for the tool '" + toolName + "' has been successfully confirmed.\n" +
                "Date: " + bookingDate + "\n\n" +
                "Thank you for choosing Lankatools!";

        // Send it out!
        emailService.sendSimpleEmail(customerEmail, subject, body);
    }
}