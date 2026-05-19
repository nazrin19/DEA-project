package com.example.Lankatools.service;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.BookingStatus;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public Booking createBooking(String customerEmail, Long toolId, LocalDate startDate, LocalDate endDate) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tool not found"));

        validateBookingDates(startDate, endDate);

        if (bookingRepository.existsByToolAndStatusNotInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                tool,
                List.of(BookingStatus.CANCELLED, BookingStatus.REJECTED),
                endDate,
                startDate
        )) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tool already has a booking during those dates");
        }

        long dayCount = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double totalCost = dayCount * tool.getDailyRate();

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTool(tool);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalCost(totalCost);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        String subject = "Lankatools Booking Created";
        String body = "Your booking request for " + tool.getName() + " has been received. " +
                "Start: " + startDate + ", End: " + endDate + ", Total: " + totalCost + ".";
        emailService.sendSimpleEmail(customerEmail, subject, body);

        return savedBooking;
    }

    public List<Booking> getBookingsForCustomer(String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        return bookingRepository.findByCustomer(customer);
    }

    public List<Booking> getBookingsForToolOwner(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
        return bookingRepository.findByToolOwner(owner);
    }

    public Booking confirmBooking(Long bookingId, String ownerEmail) {
        Booking booking = findBooking(bookingId);
        ensureToolOwner(booking, ownerEmail);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending bookings can be confirmed");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking updated = bookingRepository.save(booking);
        sendStatusEmail(updated, "Booking Confirmed", "Your booking has been confirmed.");
        return updated;
    }

    public Booking rejectBooking(Long bookingId, String ownerEmail) {
        Booking booking = findBooking(bookingId);
        ensureToolOwner(booking, ownerEmail);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending bookings can be rejected");
        }
        booking.setStatus(BookingStatus.REJECTED);
        Booking updated = bookingRepository.save(booking);
        sendStatusEmail(updated, "Booking Rejected", "Your booking request has been rejected.");
        return updated;
    }

    public Booking cancelBooking(Long bookingId, String customerEmail) {
        Booking booking = findBooking(bookingId);
        ensureCustomer(booking, customerEmail);
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.REJECTED || booking.getStatus() == BookingStatus.RETURNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This booking cannot be cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);
        sendStatusEmail(updated, "Booking Cancelled", "Your booking has been cancelled.");
        return updated;
    }

    public Booking markReturned(Long bookingId, String ownerEmail) {
        Booking booking = findBooking(bookingId);
        ensureToolOwner(booking, ownerEmail);
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.REJECTED || booking.getStatus() == BookingStatus.RETURNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This booking cannot be marked as returned");
        }
        booking.setStatus(BookingStatus.RETURNED);
        Booking updated = bookingRepository.save(booking);
        sendStatusEmail(updated, "Tool Returned", "The tool has been marked as returned.");
        return updated;
    }

    private Booking findBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    private void ensureToolOwner(Booking booking, String ownerEmail) {
        if (!booking.getTool().getOwner().getEmail().equals(ownerEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to manage this booking");
        }
    }

    private void ensureCustomer(Booking booking, String customerEmail) {
        if (!booking.getCustomer().getEmail().equals(customerEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to manage this booking");
        }
    }

    private void validateBookingDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start and end dates are required");
        }
        if (endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be on or after start date");
        }
    }

    private void sendStatusEmail(Booking booking, String subject, String content) {
        String body = "Booking #" + booking.getId() + " for " + booking.getTool().getName() + " is now " + booking.getStatus() + ".\n" +
                "Start: " + booking.getStartDate() + ", End: " + booking.getEndDate() + ".\n" +
                content;
        emailService.sendSimpleEmail(booking.getCustomer().getEmail(), subject, body);
    }
}
