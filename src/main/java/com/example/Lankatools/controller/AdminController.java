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
    @GetMapping("/users")
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
        model.addAttribute("selectedRole", role);
        return "admin/users";
    }

    @PostMapping("/users/{id}/suspend")
    public String suspendUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.toggleUserSuspension(id);
        ra.addFlashAttribute("success", "User profile status updated successfully.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/approve-owner")
    public String approveOwner(@PathVariable Long id, RedirectAttributes ra) {
        userService.approveUser(id);
        ra.addFlashAttribute("success", "Shop owner approved successfully!");
        return "redirect:/admin/users";
    }

    // DYNAMIC EQUIPMENT MODERATION DESK
    @GetMapping("/tools-moderation")
    public String showToolsModeration(Model model) {
        List<Tool> tools = toolRepository.findAll();
        List<Tool> pendingTools = toolRepository.findByStatus(Toolstatus.PENDING);
        model.addAttribute("tools", tools);
        model.addAttribute("pendingTools", pendingTools);
        model.addAttribute("approvedCount", toolRepository.findByStatus(Toolstatus.APPROVED).size());
        model.addAttribute("pendingCount", pendingTools.size());
        model.addAttribute("rejectedCount", toolRepository.findByStatus(Toolstatus.REJECTED).size());
        return "admin/tools-moderation";
    }

    @PostMapping("/tools/{id}/approve")
    public String approveTool(@PathVariable Long id, RedirectAttributes ra) {
        Tool tool = toolRepository.findById(id).orElse(null);
        if (tool != null) { tool.setStatus(Toolstatus.APPROVED); toolRepository.save(tool); }
        ra.addFlashAttribute("success", "Tool approved!");
        return "redirect:/admin/tools-moderation";
    }

    @PostMapping("/tools/{id}/reject")
    public String rejectTool(@PathVariable Long id, RedirectAttributes ra) {
        Tool tool = toolRepository.findById(id).orElse(null);
        if (tool != null) { tool.setStatus(Toolstatus.REJECTED); toolRepository.save(tool); }
        ra.addFlashAttribute("success", "Tool rejected.");
        return "redirect:/admin/tools-moderation";
    }

    @GetMapping("/tools/pending")
    public String pendingToolsRedirect() {
        return "redirect:/admin/tools-moderation";
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
    public String showRegisteredShops(Model model) {
        List<User> shopOwners = userRepository.findByRole(Role.SHOP_OWNER);
        model.addAttribute("shopOwners", shopOwners);
        long approved = shopOwners.stream().filter(u -> Boolean.TRUE.equals(u.getApproved())).count();
        model.addAttribute("approvedShopCount", approved);
        model.addAttribute("pendingShopCount", shopOwners.size() - approved);
        return "admin/shops-list";
    }

    @PostMapping("/shops/{id}/approve")
    public String approveShop(@PathVariable Long id, RedirectAttributes ra) {
        userService.approveUser(id);
        ra.addFlashAttribute("success", "Shop approved!");
        return "redirect:/admin/shops-list";
    }

    @PostMapping("/shops/{id}/suspend")
    public String suspendShop(@PathVariable Long id, RedirectAttributes ra) {
        userService.toggleUserSuspension(id);
        ra.addFlashAttribute("success", "Shop status updated.");
        return "redirect:/admin/shops-list";
    }

    // 🎯 NEW: SERVE THE SHOP DETAILS MANAGEMENT PROFILE
    // This perfectly hooks up to templates/admin/shop-detail.html
    @GetMapping("/shop-detail")
    public String showShopDetail(@RequestParam(required = false) Long id, Model model) {
        // We pass the parameter ID straight down to the view so the internal
        // JavaScript engine can extract the correct query context from the URL!
        model.addAttribute("ownerId", id);
        return "admin/shop-detail";
    }
}