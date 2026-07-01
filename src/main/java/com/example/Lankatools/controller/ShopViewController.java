package com.example.Lankatools.controller;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.enums.Role;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/owner")
public class ShopViewController {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 🎯 UPLOAD NEW TOOL FORM
     */
    @GetMapping("/upload-tool")
    public String showUploadToolForm() {
        return "owner/upload-tool";
    }

    /**
     * 🎯 VIEW OWNER EQUIPMENT INVENTORY
     */
    @GetMapping({"/tools", "/view-equipment", "/my-tool"})
    public String viewEquipment(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User owner = userRepository.findByEmail(username).orElse(null);

        // 🎯 FIX: Solved lambda final variable rule by executing a clean fallback check
        if (owner == null) {
            owner = userRepository.findAll().stream()
                    .filter(u -> username.equals(u.getName()) && u.getRole() == Role.SHOP_OWNER)
                    .findFirst()
                    .orElse(null);
        }

        if (owner == null) {
            return "redirect:/login";
        }

        List<Tool> ownerTools = toolRepository.findByOwner(owner);
        model.addAttribute("tools", ownerTools);

        return "owner/my-tool";
    }

    /**
     * 🎯 REVIEW BOOKINGS TIMELINE
     */
    @GetMapping({"/bookings", "/review-bookings", "/rental-requests"})
    public String reviewBookings(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User owner = userRepository.findByEmail(username).orElse(null);

        if (owner == null) {
            owner = userRepository.findAll().stream()
                    .filter(u -> username.equals(u.getName()) && u.getRole() == Role.SHOP_OWNER)
                    .findFirst()
                    .orElse(null);
        }

        if (owner == null) {
            return "redirect:/login";
        }

        // 🎯 Find all tools belonging to this owner first
        List<Tool> ownerTools = toolRepository.findByOwner(owner);

        // 🎯 Safely filter bookings whose tools belong to this owner's tool list
        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> ownerBookings = allBookings.stream()
                .filter(b -> b.getTool() != null && ownerTools.contains(b.getTool()))
                .collect(Collectors.toList());

        model.addAttribute("bookings", ownerBookings);

        return "owner/rental-requests";
    }
}