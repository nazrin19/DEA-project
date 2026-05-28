package com.example.Lankatools.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.Lankatools.entity.Booking;

@Service
public class BookingService {

    // An in-memory database storage for bookings
    private final List<Booking> bookingStorage = new ArrayList<>();

    public Booking createBooking(String customerName, Long toolId, LocalDate startDate, LocalDate endDate) {
        Booking booking = new Booking();
        booking.setId(toolId);
        System.out.println("Creating booking for customer: " + customerName + " from " + startDate + " to " + endDate);
        bookingStorage.add(booking);
        return booking;
    }

    public Booking confirmBooking(Long id, String username) {
        System.out.println("Booking ID " + id + " confirmed by owner: " + username);
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }

    public Booking rejectBooking(Long id, String username) {
        System.out.println("Booking ID " + id + " rejected by owner: " + username);
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }

    public Booking cancelBooking(Long id, String username) {
        System.out.println("Booking ID " + id + " cancelled by user: " + username);
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }

    public Booking markReturned(Long id, String username) {
        System.out.println("Tool for booking ID " + id + " marked returned by user: " + username);
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }

    public List<Booking> getBookingsForCustomer(String username) {
        System.out.println("Fetching bookings for customer: " + username);
        return bookingStorage;
    }

    public List<Booking> getBookingsForToolOwner(String username) {
        System.out.println("Fetching tool inventory bookings for owner: " + username);
        return bookingStorage;
    }
}