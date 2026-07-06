package com.example.Lankatools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmation(String toEmail, String customerName, String toolName, String startDate, String endDate, double totalCost) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your-email@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Booking Confirmed - LankaTools");

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "Thank you for your order! Your booking request for the '%s' has been successfully placed.\n\n" +
                            "📋 Rental Details:\n" +
                            "- Pick-up Date: %s\n" +
                            "- Return Date: %s\n" +
                            "- Total Cost: Rs. %.2f\n\n" +
                            "You will receive an update once the shop owner reviews your rental request.\n\n" +
                            "Best regards,\n" +
                            "Team LankaTools",
                    customerName, toolName, startDate, endDate, totalCost
            );

            message.setText(emailBody);
            mailSender.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send email layout notification: " + e.getMessage());
        }
    }
}