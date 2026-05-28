package com.example.Lankatools.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.service.BookingService;
import com.example.Lankatools.service.EmailService;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/bookings")
    public String handleNewBooking(@RequestParam Long toolId,
                                   @RequestParam String startDate,
                                   @RequestParam String endDate,
                                   Principal principal) {
        LocalDate parsedStart = LocalDate.parse(startDate);
        LocalDate parsedEnd = LocalDate.parse(endDate);
        bookingService.createBooking(principal.getName(), toolId, parsedStart, parsedEnd);
        return "redirect:/";
    }

    @PostMapping("/api/bookings")
    @ResponseBody
    public Booking createBooking(@RequestBody BookingRequest request, Principal principal) {
        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());

        Booking newBooking = bookingService.createBooking(principal.getName(), request.getToolId(), startDate, endDate);

        if (newBooking != null) {
            String ownerEmail = "owner-placeholder@gmail.com";
            String subject = "🔔 New Booking Request Pending Approval";
            String body = """
                    Dear Partner,
                    
                    A customer has requested to rent a tool from your inventory.
                    Please log into your LankaTools Dashboard to process this request.
                    
                    LankaTools System""";
            emailService.sendSimpleEmail(ownerEmail, subject, body);
        }
        return newBooking;
    }

    @GetMapping("/api/bookings/my")
    @ResponseBody
    public List<Booking> getMyBookings(Principal principal) {
        return bookingService.getBookingsForCustomer(principal.getName());
    }

    @GetMapping("/api/bookings/owner")
    @ResponseBody
    public List<Booking> getBookingsForToolOwner(Principal principal) {
        return bookingService.getBookingsForToolOwner(principal.getName());
    }

    @PutMapping("/api/bookings/{id}/confirm")
    @ResponseBody
    public Booking confirmBooking(@PathVariable Long id, Principal principal) {
        Booking confirmedBooking = bookingService.confirmBooking(id, principal.getName());

        if (confirmedBooking != null) {
            String customerEmail = principal.getName();
            String subject = "🛠️ LankaTools - Booking Confirmed!";
            String body = """
                    Dear Customer,
                    
                    Your booking request has been APPROVED.
                    Please check your dashboard schedule details.
                    
                    Thank you!
                    LankaTools Team""";
            emailService.sendSimpleEmail(customerEmail, subject, body);
        }
        return confirmedBooking;
    }

    @PutMapping("/api/bookings/{id}/reject")
    @ResponseBody
    public Booking rejectBooking(@PathVariable Long id, Principal principal) {
        Booking rejectedBooking = bookingService.rejectBooking(id, principal.getName());

        if (rejectedBooking != null) {
            String customerEmail = principal.getName();
            String subject = "❌ LankaTools - Booking Update";
            String body = """
                    Dear Customer,
                    
                    We regret to inform you that your booking request was declined.
                    Please check the dashboard to explore alternative available tools.
                    
                    Best regards,
                    LankaTools Team""";
            emailService.sendSimpleEmail(customerEmail, subject, body);
        }
        return rejectedBooking;
    }

    @PutMapping("/api/bookings/{id}/cancel")
    @ResponseBody
    public Booking cancelBooking(@PathVariable Long id, Principal principal) {
        return bookingService.cancelBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/return")
    @ResponseBody
    public Booking markReturned(@PathVariable Long id, Principal principal) {
        return bookingService.markReturned(id, principal.getName());
    }

    // Adding this annotation satisfies the Lombok inspection entirely!
    @lombok.Getter
    @lombok.Setter
    public static class BookingRequest {
        private Long toolId;
        private String startDate;
        private String endDate;
    }
}