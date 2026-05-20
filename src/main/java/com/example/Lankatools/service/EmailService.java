package com.example.Lankatools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        // This is the sender address
        message.setFrom("your-system-email@gmail.com");
        // This is the customer's or user's email address
        message.setTo(toEmail);
        // This is the email headline text
        message.setSubject(subject);
        // This is the actual main message text
        message.setText(body);

        // This line physically pushes the email out to the web
        mailSender.send(message);
        System.out.println("Email sent successfully to " + toEmail);
    }
}