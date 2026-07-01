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
import org.thymeleaf.context.Context;

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
        Booking newBooking = bookingService.createBooking(principal.getName(), toolId, parsedStart, parsedEnd);

        // FIX: Replaced .getUser() with .getOwner() to match the Tool entity
        if (newBooking != null && newBooking.getTool() != null && newBooking.getTool().getOwner() != null) {
            Context context = new Context();
            context.setVariable("toolName", newBooking.getTool().getName());
            String ownerEmail = newBooking.getTool().getOwner().getEmail();
            emailService.sendEmail(ownerEmail, "🔔 New Booking Request Pending Approval", "new-booking", context);
        }
        return "redirect:/";
    }

    @PostMapping("/api/bookings")
    @ResponseBody
    public Booking createBooking(@RequestBody BookingRequest request, Principal principal) {
        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());

        Booking newBooking = bookingService.createBooking(principal.getName(), request.getToolId(), startDate, endDate);

        // FIX: Replaced .getUser() with .getOwner() to match the Tool entity
        if (newBooking != null && newBooking.getTool() != null && newBooking.getTool().getOwner() != null) {
            Context context = new Context();
            context.setVariable("toolName", newBooking.getTool().getName());

            String ownerEmail = newBooking.getTool().getOwner().getEmail();
            emailService.sendEmail(ownerEmail, "🔔 New Booking Request Pending Approval", "new-booking", context);
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

        if (confirmedBooking != null && confirmedBooking.getCustomer() != null) {
            Context context = new Context();
            context.setVariable("toolName", confirmedBooking.getTool().getName());

            String customerEmail = confirmedBooking.getCustomer().getEmail();
            emailService.sendEmail(customerEmail, "🛠️ LankaTools - Booking Confirmed!", "booking-confirmed", context);
        }
        return confirmedBooking;
    }

    @PutMapping("/api/bookings/{id}/reject")
    @ResponseBody
    public Booking rejectBooking(@PathVariable Long id, Principal principal) {
        Booking rejectedBooking = bookingService.rejectBooking(id, principal.getName());

        if (rejectedBooking != null && rejectedBooking.getCustomer() != null) {
            Context context = new Context();
            context.setVariable("toolName", rejectedBooking.getTool().getName());

            String customerEmail = rejectedBooking.getCustomer().getEmail();
            emailService.sendEmail(customerEmail, "❌ LankaTools - Booking Update", "booking-rejected", context);
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

    @lombok.Getter
    @lombok.Setter
    public static class BookingRequest {
        private Long toolId;
        private String startDate;
        private String endDate;
    }
}