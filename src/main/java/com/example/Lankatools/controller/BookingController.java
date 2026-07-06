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
import com.example.Lankatools.service.EmailService; // Imported EmailService

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private EmailService emailService; // Injected EmailService

    /**
     * 1. STANDARD FORM SUBMISSION GATEWAY (Thymeleaf UI Form Post Action)
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

            bookingService.createBooking(principal.getName(), toolId, parsedStart, parsedEnd);

        } catch (IllegalArgumentException e) {
            System.err.println("Booking blocked due to business rule validation: " + e.getMessage());
            return "redirect:/?error=dates_overlap";
        } catch (Exception e) {
            System.err.println("Core booking application error: " + e.getMessage());
            return "redirect:/?error=booking_failed";
        }

        return "redirect:/customer/bookings";
    }

    /**
     * 👑 OWNER FORM ACTION ENDPOINTS (Thymeleaf Dashboard UI Actions)
     */
    @PostMapping("/owner/bookings/{id}/approve")
    public String approveRentalBooking(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            // 1. Confirm the booking status to database (sets to CONFIRMED)
            bookingService.confirmBooking(id, principal.getName());
            System.out.println("🚀 UI Action: Booking ID " + id + " approved (CONFIRMED) successfully.");

            // 2. Fetch the fully mapped booking object to gather strings safely
            Booking updatedBooking = bookingService.findById(id);
            if (updatedBooking != null && updatedBooking.getCustomer() != null && updatedBooking.getTool() != null) {

                // 3. Dispatch the real-time confirmation email over secure SMTP
                emailService.sendBookingConfirmation(
                        updatedBooking.getCustomer().getEmail(),
                        updatedBooking.getCustomer().getName(),
                        updatedBooking.getTool().getName(),
                        updatedBooking.getStartDate().toString(),
                        updatedBooking.getEndDate().toString(),
                        updatedBooking.getTotalCost()
                );
                System.out.println("📬 Success: Approval confirmation email dispatched to " + updatedBooking.getCustomer().getEmail());
            }
        } catch (Exception e) {
            System.err.println("Error processing booking approval/email dispatch via UI: " + e.getMessage());
        }

        return "redirect:/owner/rental-requests";
    }

    @PostMapping("/owner/bookings/{id}/reject")
    public String rejectRentalBooking(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            bookingService.rejectBooking(id, principal.getName());
            System.out.println("🛡️ UI Action: Booking ID " + id + " marked REJECTED.");
        } catch (Exception e) {
            System.err.println("Error processing booking rejection via UI: " + e.getMessage());
        }

        return "redirect:/owner/rental-requests";
    }

    /**
     * 🎯 DEDICATED THYMELEAF VIEW ROUTE
     */
    @GetMapping("/customer/bookings")
    public String showCustomerBookingsPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<Booking> customerBookings = bookingService.getBookingsForCustomer(principal.getName());
        model.addAttribute("bookings", customerBookings);

        return "customer/bookings";
    }

    // DEDICATED CALCULATOR CHECKOUT GATEWAY VIEW ROUTE
    @GetMapping("/bookings/checkout")
    public String showCheckoutPage(@RequestParam Long toolId, Model model) {
        Tool tool = toolService.getToolById(toolId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool Id: " + toolId));

        model.addAttribute("tool", tool);
        return "checkout";
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

        public Long getToolId() { return toolId; }
        public void setToolId(Long toolId) { this.toolId = toolId; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
    }
}