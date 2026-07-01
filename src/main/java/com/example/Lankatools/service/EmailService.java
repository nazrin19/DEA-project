package com.example.Lankatools.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    // Requirement: Create EmailService with sendEmail method
    public void sendEmail(String toEmail, String subject, String templateName, Context context) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Renders clean, dynamic HTML template engine layouts
            String htmlContent = templateEngine.process(templateName, context);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true sets the type to text/html

            mailSender.send(mimeMessage);
            System.out.println("📬 Spring Mail dispatched successfully to: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("❌ Spring Mail execution failure: " + e.getMessage());
        }
    }
}