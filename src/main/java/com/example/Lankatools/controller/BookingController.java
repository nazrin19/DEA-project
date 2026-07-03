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
<<<<<<< HEAD
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
=======
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

        return "redirect:/customer/bookings"; // Redirects customer straight to their history after booking!
>>>>>>> 9d50a90a1a4b085d2e11fc5fc128caa86e7e9c89
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