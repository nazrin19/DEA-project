package com.example.Lankatools.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.service.BookingService;
import com.example.Lankatools.service.ToolService;

@Controller
public class GeneralViewController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ToolService toolService;

    /**
     * 🎯 CUSTOMER DASHBOARD
     * Serves the customer dashboard template view seamlessly.
     */
    @GetMapping("/customer/dashboard")
    public String showCustomerDashboard() {
        return "customer/dashboard"; // Maps to templates/customer/dashboard.html
    }

    /**
     * 🎯 OWNER DASHBOARD (WITH LIVE METRICS)
     * Keeps your working path active while loading real database statistics.
     */
    @GetMapping("/owner/dashboard")
    public String showOwnerDashboard(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String ownerEmail = principal.getName();

        // 1. Fetch records safely from services
        List<Booking> ownerBookings = bookingService.getBookingsForToolOwner(ownerEmail);
        List<Tool> allTools = toolService.getAllTools();

        // 2. Filter data down to calculate metrics programmatically
        long ownedToolsCount = 0;
        if (allTools != null) {
            ownedToolsCount = allTools.stream()
                    .filter(t -> t.getOwner() != null && ownerEmail.equalsIgnoreCase(t.getOwner().getEmail()))
                    .count();
        }

        long activeBookingsCount = 0;
        double earningsCalculated = 0.0;

        if (ownerBookings != null) {
            activeBookingsCount = ownerBookings.stream()
                    .filter(b -> b.getStatus() != null && "CONFIRMED".equalsIgnoreCase(b.getStatus().name()))
                    .count();

            earningsCalculated = ownerBookings.stream()
                    .filter(b -> b.getStatus() != null && "CONFIRMED".equalsIgnoreCase(b.getStatus().name()))
                    .mapToDouble(Booking::getTotalCost)
                    .sum();
        }

        // 3. Populate layout variables matching templates/owner/dashboard.html
        model.addAttribute("toolsCount", ownedToolsCount);
        model.addAttribute("bookingsCount", activeBookingsCount);
        model.addAttribute("totalEarnings", earningsCalculated);

        return "owner/dashboard"; // Maps to templates/owner/dashboard.html
    }
}