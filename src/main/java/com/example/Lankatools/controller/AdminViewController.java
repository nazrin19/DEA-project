package com.example.Lankatools.controller;

import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.*;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @Autowired private UserRepository userRepository;
    @Autowired private ToolRepository toolRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private ToolService toolService;


    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalTools", toolRepository.count());
        model.addAttribute("totalBookings", bookingRepository.count());
        model.addAttribute("pendingApprovals", toolService.getToolsByStatus(Toolstatus.PENDING).size());
        return "admin/dashboard";
    }


    @GetMapping("/shops-list")
    public String showRegisteredShops() {
        return "admin/shops-list";
    }


    @GetMapping("/tools-moderation")
    public String showToolsModeration() {
        return "admin/tools-moderation";
    }
}