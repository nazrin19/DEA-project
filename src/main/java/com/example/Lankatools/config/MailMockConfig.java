package com.example.Lankatools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;

@Configuration
public class MailMockConfig {

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                System.out.println("\n==================================================");
                System.out.println("📬 [MOCK EMAIL TRIGGERED SUCCESSFULLY]");
                System.out.println("To: " + String.join(", ", simpleMessage.getTo())); // Capitalized T
                System.out.println("Subject: " + simpleMessage.getSubject());
                System.out.println("Body: " + simpleMessage.getText());
                System.out.println("==================================================\n");
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {
                for (SimpleMailMessage msg : simpleMessages) {
                    send(msg);
                }
            }
        };
    }
}