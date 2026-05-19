package com.example.Lankatools.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.enums.Bookingstatus;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.ToolRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private EmailService emailService;

    public Booking createBooking(Long toolId, String customerName, String customerEmail,
                                 LocalDate startDate, LocalDate endDate) {
        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new IllegalArgumentException("Tool not found."));

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be the same or after the start date.");
        }

        List<Booking> overlappingBookings = bookingRepository
                .findOverlappingBookings(
                        toolId,
                        Arrays.asList(Bookingstatus.PENDING, Bookingstatus.CONFIRMED),
                        startDate,
                        endDate
                );

        if (!overlappingBookings.isEmpty()) {
            throw new IllegalArgumentException("Selected dates overlap with an existing booking.");
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double totalCost = days * tool.getDailyRate();

        Booking booking = new Booking();
        booking.setTool(tool);
        booking.setCustomerName(customerName);
        booking.setCustomerEmail(customerEmail);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setStatus(Bookingstatus.PENDING);
        booking.setTotalCost(totalCost);

        Booking savedBooking = bookingRepository.save(booking);
        sendConfirmationEmail(savedBooking);
        return savedBooking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Tool> getApprovedTools() {
        return toolRepository.findByStatus(Toolstatus.APPROVED);
    }

    private void sendConfirmationEmail(Booking booking) {
        String subject = "Lankatools Booking Confirmation";
        String body = "Dear " + booking.getCustomerName() + ",\n\n" +
                "Your booking for the tool '" + booking.getTool().getName() + "' is confirmed as pending.\n" +
                "Booking dates: " + booking.getStartDate() + " to " + booking.getEndDate() + "\n" +
                "Total cost: $" + booking.getTotalCost() + "\n\n" +
                "We will contact you once the booking is finalized.\n\n" +
                "Thank you for choosing Lankatools.";

        emailService.sendSimpleEmail(booking.getCustomerEmail(), subject, body);
    }
}
