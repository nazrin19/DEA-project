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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;


    // DASHBOARD STATS
    @GetMapping("/dashboard")
    public AdminStatsDto dashboardStats() {
        long totalUsers = userRepository.count();
        long totalTools = toolRepository.count();
        long totalBookings = bookingRepository.count();
        int pendingCount = toolRepository.findByStatus(Toolstatus.PENDING).size();

        return new AdminStatsDto(totalUsers, totalTools, totalBookings, pendingCount);
    }

    // ALL USERS (optionally filtered by role)
    @GetMapping("/users")
    public List<User> allUsers(@RequestParam(required = false) String role) {
        if (role != null && !role.isEmpty()) {
            try {
                Role roleEnum = Role.valueOf(role);
                return userRepository.findByRole(roleEnum);
            } catch (IllegalArgumentException e) {
                return userRepository.findAll();
            }
        }
        return userRepository.findAll();
    }

    // ALL TOOLS (for moderation)
    @GetMapping("/tools")
    public List<Tool> allTools() {
        return toolRepository.findAll();
    }

    // PENDING TOOLS ONLY
    @GetMapping("/tools/pending")
    public List<Tool> pendingTools() {
        return toolRepository.findByStatus(Toolstatus.PENDING);
    }

    // ALL BOOKINGS
    @GetMapping("/bookings")
    public List<Booking> allBookings() {
        return bookingRepository.findAll();
    }

    // SHOP OWNERS LIST
    @GetMapping("/shops")
    public List<User> allShopOwners() {
        return userRepository.findByRole(Role.SHOP_OWNER);
    }


    // SUSPEND / UNSUSPEND USER
    @PostMapping("/users/{id}/suspend")
    public ResponseEntity<Map<String, String>> suspendUser(@PathVariable Long id) {
        userService.toggleUserSuspension(id);
        return ResponseEntity.ok(Map.of("message", "User status updated successfully."));
    }

    // APPROVE SHOP OWNER
    @PostMapping("/users/{id}/approve-owner")
    public ResponseEntity<Map<String, String>> approveOwner(@PathVariable Long id) {
        userService.approveUser(id);
        return ResponseEntity.ok(Map.of("message", "Shop owner approved successfully."));
    }

    // APPROVE TOOL
    @PostMapping("/tools/{id}/approve")
    public ResponseEntity<Map<String, String>> approveTool(@PathVariable Long id) {
        Tool tool = toolRepository.findById(id).orElse(null);
        if (tool == null) {
            return ResponseEntity.notFound().build();
        }
        tool.setStatus(Toolstatus.APPROVED);
        toolRepository.save(tool);
        return ResponseEntity.ok(Map.of("message", "Tool approved."));
    }

    // REJECT TOOL
    @PostMapping("/tools/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectTool(@PathVariable Long id) {
        Tool tool = toolRepository.findById(id).orElse(null);
        if (tool == null) {
            return ResponseEntity.notFound().build();
        }
        tool.setStatus(Toolstatus.REJECTED);
        toolRepository.save(tool);
        return ResponseEntity.ok(Map.of("message", "Tool rejected."));
    }

    // APPROVE SHOP
    @PostMapping("/shops/{id}/approve")
    public ResponseEntity<Map<String, String>> approveShop(@PathVariable Long id) {
        userService.approveUser(id);
        return ResponseEntity.ok(Map.of("message", "Shop approved."));
    }

    // SUSPEND SHOP
    @PostMapping("/shops/{id}/suspend")
    public ResponseEntity<Map<String, String>> suspendShop(@PathVariable Long id) {
        userService.toggleUserSuspension(id);
        return ResponseEntity.ok(Map.of("message", "Shop status updated."));
    }
}