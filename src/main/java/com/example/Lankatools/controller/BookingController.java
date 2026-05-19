package com.example.Lankatools.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody BookingRequest request, Principal principal) {
        LocalDate startDate = LocalDate.parse(request.getStartDate());
        LocalDate endDate = LocalDate.parse(request.getEndDate());
        return bookingService.createBooking(principal.getName(), request.getToolId(), startDate, endDate);
    }

    @GetMapping("/my")
    public List<Booking> getMyBookings(Principal principal) {
        return bookingService.getBookingsForCustomer(principal.getName());
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsForToolOwner(Principal principal) {
        return bookingService.getBookingsForToolOwner(principal.getName());
    }

    @PutMapping("/{id}/confirm")
    public Booking confirmBooking(@PathVariable Long id, Principal principal) {
        return bookingService.confirmBooking(id, principal.getName());
    }

    @PutMapping("/{id}/reject")
    public Booking rejectBooking(@PathVariable Long id, Principal principal) {
        return bookingService.rejectBooking(id, principal.getName());
    }

    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id, Principal principal) {
        return bookingService.cancelBooking(id, principal.getName());
    }

    @PutMapping("/{id}/return")
    public Booking markReturned(@PathVariable Long id, Principal principal) {
        return bookingService.markReturned(id, principal.getName());
    }

    public static class BookingRequest {
        private Long toolId;
        private String startDate;
        private String endDate;

        public Long getToolId() {
            return toolId;
        }

        public void setToolId(Long toolId) {
            this.toolId = toolId;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }
}
