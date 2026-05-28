package com.example.Lankatools.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Lankatools.entity.Booking;

@Service
public class BookingService {

    @Autowired
    private EmailService emailService;

    public void createBooking(String customerEmail, String toolName, String bookingDate) {
        System.out.println("Booking saved in system for tool: " + toolName);
        String subject = "🛠️ Lankatools - Booking Confirmation!";
        String body = "Dear Customer,\n\nYour booking for '" + toolName + "' has been successfully confirmed.";
        emailService.sendSimpleEmail(customerEmail, subject, body);
    }

    public Booking createBooking(String customerName, Long toolId, LocalDate startDate, LocalDate endDate) {
        Booking mockBooking = new Booking();
        mockBooking.setId(toolId);
        return mockBooking;
    }

    public Booking confirmBooking(Long id, String username) {
        Booking mockBooking = new Booking();
        mockBooking.setId(id);
        return mockBooking;
    }

    public Booking rejectBooking(Long id, String username) {
        Booking mockBooking = new Booking();
        mockBooking.setId(id);
        return mockBooking;
    }

    public Booking cancelBooking(Long id, String username) {
        Booking mockBooking = new Booking();
        mockBooking.setId(id);
        return mockBooking;
    }

    public Booking markReturned(Long id, String username) {
        Booking mockBooking = new Booking();
        mockBooking.setId(id);
        return mockBooking;
    }

    public List<Booking> getBookingsForCustomer(String username) {
        return new ArrayList<>();
    }

    public List<Booking> getBookingsForToolOwner(String username) {
        return new ArrayList<>();
    }
}