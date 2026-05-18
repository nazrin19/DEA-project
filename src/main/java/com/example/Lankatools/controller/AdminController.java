package com.example.Lankatools.controller;

import com.example.Lankatools.dto.AdminStatsDto;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Role;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.repository.UserRepository;
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
    private UserService userService;

    //DASHBOARD

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        AdminStatsDto stats = new AdminStatsDto(
                userRepository.count(),
                toolRepository.count(),
                0, // bookings → update when Person 3 done
                toolRepository.findByStatus(Toolstatus.PENDING).size()
        );
        model.addAttribute("stats", stats);
        return "admin/dashboard";
    }

    //USERS

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
    public String suspendUser(
            @PathVariable Long id,
            RedirectAttributes ra) {
        userService.suspend(id);
        ra.addFlashAttribute("success", "User suspended.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/approve-owner")
    public String approveOwner(
            @PathVariable Long id,
            RedirectAttributes ra) {
        userService.approveShopOwner(id);
        ra.addFlashAttribute("success", "Shop owner approved!");
        return "redirect:/admin/users";
    }

    //PENDING TOOLS

    @GetMapping("/tools/pending")
    public String pendingTools(Model model) {
        List<Tool> tools = toolRepository
                .findByStatus(Toolstatus.PENDING);
        model.addAttribute("tools", tools);
        return "admin/pending-tools";
    }

    @PostMapping("/tools/{id}/approve")
    public String approveTool(
            @PathVariable Long id,
            RedirectAttributes ra) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tool not found"));
        tool.setStatus(Toolstatus.APPROVED);
        toolRepository.save(tool);
        ra.addFlashAttribute("success", "Tool approved!");
        return "redirect:/admin/tools/pending";
    }

    @PostMapping("/tools/{id}/reject")
    public String rejectTool(
            @PathVariable Long id,
            RedirectAttributes ra) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tool not found"));
        tool.setStatus(Toolstatus.REJECTED);
        toolRepository.save(tool);
        ra.addFlashAttribute("success", "Tool rejected.");
        return "redirect:/admin/tools/pending";
    }

    //BOOKINGS
    @GetMapping("/bookings")
    public String allBookings(
            @RequestParam(required = false) String status,
            Model model) {
        // Will update when Person 3 done
        model.addAttribute("bookings", List.of());
        model.addAttribute("selectedStatus", "");
        return "admin/bookings";
    }

}
