package com.example.Lankatools.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.service.BookingService;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String listBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<Tool> tools = bookingService.getApprovedTools();
        model.addAttribute("tools", tools);
        return "bookings/create";
    }

    @PostMapping("/create")
    public String handleNewBooking(
            @RequestParam("toolId") Long toolId,
            @RequestParam("customerName") String customerName,
            @RequestParam("customerEmail") String customerEmail,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            bookingService.createBooking(toolId, customerName, customerEmail, start, end);
            redirectAttributes.addFlashAttribute("success", "Booking created successfully.");
            return "redirect:/bookings";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/bookings/create";
        }
    }
}
