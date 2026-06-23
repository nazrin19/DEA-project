package com.example.Lankatools.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        try {
            LocalDate parsedStart = LocalDate.parse(startDate);
            LocalDate parsedEnd = LocalDate.parse(endDate);
            bookingService.createBooking(principal.getName(), toolId, parsedStart, parsedEnd);
            redirectAttributes.addFlashAttribute("successMessage", "Booking request submitted successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/tools/" + toolId;
    }

    @GetMapping("/bookings/my")
    public String myBookings(Model model, Principal principal) {
        List<Booking> bookings = bookingService.getBookingsForCustomer(principal.getName());
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }

    @GetMapping("/bookings/incoming")
    public String incomingBookings(Model model, Principal principal) {
        List<Booking> bookings = bookingService.getBookingsForToolOwner(principal.getName());
        model.addAttribute("bookings", bookings);
        return "owner-bookings";
    }

    @PostMapping("/bookings/{id}/confirm")
    public String confirmBooking(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            bookingService.confirmBooking(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Booking confirmed.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/bookings/incoming";
    }

    @PostMapping("/bookings/{id}/reject")
    public String rejectBooking(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            bookingService.rejectBooking(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Booking rejected.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/bookings/incoming";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/bookings/my";
    }

    @PostMapping("/bookings/{id}/return")
    public String markReturned(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            bookingService.markReturned(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Booking marked as returned.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/bookings/my";
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
    public Booking confirmBookingApi(@PathVariable Long id, Principal principal) {
        return bookingService.confirmBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/reject")
    @ResponseBody
    public Booking rejectBookingApi(@PathVariable Long id, Principal principal) {
        return bookingService.rejectBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/cancel")
    @ResponseBody
    public Booking cancelBookingApi(@PathVariable Long id, Principal principal) {
        return bookingService.cancelBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/return")
    @ResponseBody
    public Booking markReturnedApi(@PathVariable Long id, Principal principal) {
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
