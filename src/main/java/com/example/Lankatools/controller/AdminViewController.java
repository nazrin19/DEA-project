package com.example.Lankatools.controller;

import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.repository.UserRepository;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ToolService toolService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // 1. Fetch count summary metrics for the Thymeleaf view dashboard cards
        long totalUsers = userRepository.count();
        long totalTools = toolRepository.count();
        long totalBookings = bookingRepository.count();
        long pendingApprovals = toolService.getToolsByStatus(Toolstatus.PENDING).size();

        // 2. Map metrics into the Thymeleaf Model Attribute Engine
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalTools", totalTools);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("pendingApprovals", pendingApprovals);

        // 3. Looks for 'src/main/resources/templates/admin/dashboard.html'
        return "admin/dashboard";
    }
}
