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

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

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
        return bookingService.createBooking(principal.getName(), request.getToolId(), startDate, endDate);
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
        return bookingService.confirmBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/reject")
    @ResponseBody
    public Booking rejectBooking(@PathVariable Long id, Principal principal) {
        return bookingService.rejectBooking(id, principal.getName());
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
