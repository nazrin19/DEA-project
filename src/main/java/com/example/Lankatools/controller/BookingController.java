package com.example.Lankatools.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.service.BookingService;
import com.example.Lankatools.service.ToolService;
import com.example.Lankatools.service.EmailService;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/bookings")
    public String handleNewBooking(@RequestParam("toolId") Long toolId,
                                   @RequestParam("startDate") String startDate,
                                   @RequestParam("endDate") String endDate,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {

        if (principal == null) {
            System.err.println("Booking rejection: No authenticated session found.");
            return "redirect:/login";
        }

        System.out.println("Processing booking via UI Form submission for user: " + principal.getName());

        try {
            LocalDate parsedStart = LocalDate.parse(startDate);
            LocalDate parsedEnd = LocalDate.parse(endDate);


            bookingService.createBooking(principal.getName(), toolId, parsedStart, parsedEnd);


            redirectAttributes.addFlashAttribute("success", "Your booking request was submitted successfully! Awaiting owner validation.");

        } catch (IllegalArgumentException e) {
            System.err.println("Booking blocked due to business rule validation: " + e.getMessage());

            redirectAttributes.addFlashAttribute("error", "The selected dates overlap with an active booking for this tool.");
            return "redirect:/bookings/checkout?toolId=" + toolId;
        } catch (Exception e) {
            System.err.println("Core booking application error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Core system error: Unable to process booking.");
            return "redirect:/bookings/checkout?toolId=" + toolId;
        }

        return "redirect:/customer/bookings";
    }


    @PostMapping("/owner/bookings/{id}/approve")
    public String approveRentalBooking(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            // 1. Confirm the booking status inside the database (sets to CONFIRMED)
            bookingService.confirmBooking(id, principal.getName());
            System.out.println("🚀 UI Action: Booking ID " + id + " approved (CONFIRMED) successfully.");

            // 2. Fetch the fully mapped booking object to gather profile details securely
            Booking updatedBooking = bookingService.findById(id);
            if (updatedBooking != null && updatedBooking.getCustomer() != null && updatedBooking.getTool() != null) {

                // 3. Dispatch real-time validation confirmations over SMTP mailer configurations
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


    @PostMapping("/api/bookings")
    @ResponseBody
    public Booking createBooking(@RequestBody BookingRequest request, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated session required.");
        }
        try {
            LocalDate startDate = LocalDate.parse(request.getStartDate());
            LocalDate endDate = LocalDate.parse(request.getEndDate());
            return bookingService.createBooking(principal.getName(), request.getToolId(), startDate, endDate);
        } catch (IllegalArgumentException e) {
            // Converts internal business errors to clean HTTP 400 Bad Request responses instead of ugly 500 errors
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Core runtime error handling registration.");
        }
    }

    @GetMapping("/api/bookings/my")
    @ResponseBody
    public List<Booking> getMyBookings(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return bookingService.getBookingsForCustomer(principal.getName());
    }

    @GetMapping("/api/bookings/owner")
    @ResponseBody
    public List<Booking> getBookingsForToolOwner(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return bookingService.getBookingsForToolOwner(principal.getName());
    }

    @PutMapping("/api/bookings/{id}/confirm")
    @ResponseBody
    public Booking confirmBooking(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return bookingService.confirmBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/reject")
    @ResponseBody
    public Booking rejectBooking(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return bookingService.rejectBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/cancel")
    @ResponseBody
    public Booking cancelBooking(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return bookingService.cancelBooking(id, principal.getName());
    }

    @PutMapping("/api/bookings/{id}/return")
    @ResponseBody
    public Booking markReturned(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
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