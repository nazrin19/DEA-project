package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.enums.BookingStatus;
import com.example.Lankatools.repository.BookingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
public class NotificationController {

    private final BookingRepository bookingRepository;

    public NotificationController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/notifications")
    public String showNotifications(Model model) {
        List<Booking> pendingBookings = bookingRepository.findByStatus(BookingStatus.PENDING);
        long pendingCount = bookingRepository.countByStatus(BookingStatus.PENDING);

        model.addAttribute("bookings", pendingBookings);
        model.addAttribute("notificationCount", pendingCount);

        return "notifications";
    }

    @PostMapping("/owner/bookings/approve/{id}")
    public String approveBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            redirectAttributes.addFlashAttribute("successMessage", "Booking request confirmed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not find booking item.");
        }
        return "redirect:/notifications";
    }

    @PostMapping("/owner/bookings/reject/{id}")
    public String rejectBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);
            redirectAttributes.addFlashAttribute("successMessage", "Booking request has been rejected.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not find booking item.");
        }
        return "redirect:/notifications";
    }
}