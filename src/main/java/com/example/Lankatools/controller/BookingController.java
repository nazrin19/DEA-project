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

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.service.BookingService;
import com.example.Lankatools.service.ToolService;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ToolService toolService;

    /**
     * 1. STANDARD FORM SUBMISSION GATEWAY (Thymeleaf UI Form Post Action)
     * Processes requests directly from your booking-form.html template submission.
     */
    @PostMapping("/bookings")
    public String handleNewBooking(@RequestParam("toolId") Long toolId,
                                   @RequestParam("startDate") String startDate,
                                   @RequestParam("endDate") String endDate,
                                   Principal principal) {

        if (principal == null) {
            System.err.println("Booking rejection: No authenticated session found.");
            return "redirect:/login";
        }

        System.out.println("Processing booking via UI Form submission for user: " + principal.getName());

        try {
            LocalDate parsedStart = LocalDate.parse(startDate);
            LocalDate parsedEnd = LocalDate.parse(endDate);

            // Save booking instance to database (Email notifications run internally)
            bookingService.createBooking(principal.getName(), toolId, parsedStart, parsedEnd);

        } catch (IllegalArgumentException e) {
            System.err.println("Booking blocked due to business rule validation: " + e.getMessage());
            return "redirect:/?error=dates_overlap";
        } catch (Exception e) {
            System.err.println("Core booking application error: " + e.getMessage());
            return "redirect:/?error=booking_failed";
        }

        return "redirect:/customer/bookings?success=booked"; // Redirects customer straight to their history after booking!
    }


    /**
     * 🎯 DEDICATED THYMELEAF VIEW ROUTE
     * Maps the "Track Rentals & History" arrow click from your dashboard to the UI template.
     */
    @GetMapping("/customer/bookings")
    public String showCustomerBookingsPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Fetch user bookings list from database service layer
        List<Booking> customerBookings = bookingService.getBookingsForCustomer(principal.getName());

        // Bind data collection down into Thymeleaf layout template contexts
        model.addAttribute("bookings", customerBookings);

        return "customer/bookings"; // Resolves to src/main/resources/templates/customer/bookings.html
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBookingFromPage(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            bookingService.cancelBooking(id, principal.getName());
        } catch (IllegalArgumentException e) {
            return "redirect:/customer/bookings?error=cancel_expired";
        }

        return "redirect:/customer/bookings";
    }

    @PostMapping("/owner/bookings/{id}/confirm")
    public String confirmBookingFromPage(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        bookingService.confirmBooking(id, principal.getName());
        return "redirect:/owner/rental-requests";
    }

    @PostMapping("/owner/bookings/{id}/reject")
    public String rejectBookingFromPage(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        bookingService.rejectBooking(id, principal.getName());
        return "redirect:/owner/rental-requests";
    }

    // DEDICATED CALCULATOR CHECKOUT GATEWAY VIEW ROUTE
    @GetMapping("/bookings/checkout")
    public String showCheckoutPage(@RequestParam Long toolId, Model model) {
        Tool tool = toolService.getToolById(toolId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool Id: " + toolId));

        model.addAttribute("tool", tool);
        return "checkout"; // Opens src/main/resources/templates/checkout.html
    }

    // 2. REST API ENDPOINTS SECTION (For AJAX / JavaScript Fetch Interactions)
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

    @GetMapping("/api/bookings/tool/{toolId}")
    @ResponseBody
    public List<Booking> getBookingsForTool(@PathVariable Long toolId) {
        return bookingService.getBookingsForTool(toolId);
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

    // DTO Inner Class Structure payload container mapping properties
    public static class BookingRequest {
        private Long toolId;
        private String startDate;
        private String endDate;

        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
    }
}