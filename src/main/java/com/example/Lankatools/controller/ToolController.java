package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ToolController {

    @Autowired
    private ToolService toolService;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/tools")
    public String tools(Model model) {
        model.addAttribute("tools", toolService.getToolsByStatus(Toolstatus.APPROVED));
        return "index";
    }

    @GetMapping("/tools/{id}")
    public String toolDetail(@PathVariable Long id, Model model) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tool not found."));

        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getTool() != null && booking.getTool().getId().equals(tool.getId()))
                .filter(booking -> booking.getStatus() != com.example.Lankatools.enums.BookingStatus.REJECTED
                        && booking.getStatus() != com.example.Lankatools.enums.BookingStatus.CANCELLED)
                .toList();

        List<String> bookedDates = new ArrayList<>();
        for (Booking booking : bookings) {
            LocalDate current = booking.getStartDate();
            while (!current.isAfter(booking.getEndDate())) {
                bookedDates.add(current.toString());
                current = current.plusDays(1);
            }
        }

        model.addAttribute("tool", tool);
        model.addAttribute("bookings", bookings);
        model.addAttribute("bookedDates", bookedDates);
        return "tool-detail";
    }
}
