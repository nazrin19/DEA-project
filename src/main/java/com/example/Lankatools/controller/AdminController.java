package com.example.Lankatools.controller;

import com.example.Lankatools.dto.AdminStatsDto;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.enums.Role;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.repository.UserRepository;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.service.UserService;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ToolService toolService;

    // UNIFIED DASHBOARD METRICS
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalUsers = userRepository.count();
        long totalTools = toolRepository.count();
        long totalBookings = bookingRepository.count();
        int pendingCount = toolRepository.findByStatus(Toolstatus.PENDING).size();

        AdminStatsDto stats = new AdminStatsDto(totalUsers, totalTools, totalBookings, pendingCount);
        model.addAttribute("stats", stats);

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalTools", totalTools);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("pendingApprovals", pendingCount);

        return "admin/dashboard";
    }

    // USERS MANAGEMENT
    @GetMapping({"/users", "/manage-users"})
    public String allUsers(
            @RequestParam(required = false) String role,
            Model model) {
        List<User> users;
        if (role != null && !role.isEmpty()) {
            try {
                Role roleEnum = Role.valueOf(role);
                users = userRepository.findByRole(roleEnum);
            } catch (IllegalArgumentException e) {
                users = userRepository.findAll();
            }
        } else {
            users = userRepository.findAll();
        }
        model.addAttribute("users", users);
        model.addAttribute("selectedRole", role != null ? role : "");
        return "admin/users";
    }

    @PostMapping("/users/{id}/suspend")
    public String suspendUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.toggleUserSuspension(id);
        ra.addFlashAttribute("success", "User profile status updated successfully.");
        return "redirect:/admin/manage-users";
    }

    @PostMapping("/users/{id}/approve-owner")
    public String approveOwner(@PathVariable Long id, RedirectAttributes ra) {
        userService.approveUser(id);
        ra.addFlashAttribute("success", "Shop owner approved successfully!");
        return "redirect:/admin/manage-users";
    }

    // DYNAMIC EQUIPMENT MODERATION DESK
    @GetMapping({"/tools", "/tools-moderation", "/manage-tools"})
    public String showToolsModeration(Model model) {
        model.addAttribute("tools", toolService.getAllTools());
        return "admin/tools-moderation";
    }

    @GetMapping("/tools/pending")
    public String pendingToolsRedirect() {
        return "redirect:/admin/manage-tools";
    }
    // Handles form submissions from templates/admin/tools-moderation.html cleanly
    @PostMapping("/tools/{id}/status")
    public String updateToolStatus(
            @PathVariable Long id,
            @RequestParam("status") String statusStr,
            RedirectAttributes ra) {
        try {
            Toolstatus targetStatus = Toolstatus.valueOf(statusStr.toUpperCase());
            toolService.updateToolStatus(id, targetStatus);
            ra.addFlashAttribute("success", "Tool listing has been successfully updated to " + targetStatus + "!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to update tool listing status.");
        }
        return "redirect:/admin/manage-tools";
    }

    // FULL PLATFORM RENTAL HISTORY
    @GetMapping("/bookings")
    public String allBookings(
            @RequestParam(required = false) String status,
            Model model) {
        List<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        model.addAttribute("selectedStatus", status != null ? status : "");
        return "admin/bookings";
    }

    @GetMapping("/shops-list")
    public String showRegisteredShops() {
        return "admin/shops-list";
    }

    @GetMapping("/shop-detail")
    public String showShopDetail(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("ownerId", id);
        return "admin/shop-detail";
    }
}