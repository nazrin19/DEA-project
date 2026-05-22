package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Role;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.ToolRepository;
import com.example.Lankatools.repository.UserRepository;
import com.example.Lankatools.service.ToolService;
import com.example.Lankatools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ToolService toolService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) Role role) {
        List<User> users = userService.getAllUsers(role);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<User> suspendUser(@PathVariable Long id) {
        User updatedUser = userService.toggleUserSuspension(id);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}/approve")
    public ResponseEntity<User> approveUser(@PathVariable Long id) {
        User approvedUser = userService.approveUser(id);
        if (approvedUser != null) {
            return ResponseEntity.ok(approvedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/tools/pending")
    public ResponseEntity<List<Tool>> getPendingTools(){
        List<Tool> pendingTools = toolService.getToolsByStatus(Toolstatus.PENDING);
        return ResponseEntity.ok(pendingTools);
    }

    @PutMapping("/tools/{id}/approve")
    public ResponseEntity<Tool> approveTool(@PathVariable Long id) {
        return toolRepository.findById(id).map(tool -> {
            tool.setStatus(Toolstatus.APPROVED);
            return ResponseEntity.ok(toolRepository.save(tool));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/tools/{id}/reject")
    public ResponseEntity<Tool> rejectTool(@PathVariable Long id){
        return toolRepository.findById(id).map(tool -> {
            tool.setStatus(Toolstatus.REJECTED);
            return ResponseEntity.ok(toolRepository.save(tool)); // Fixed: changed .find to .ok
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/bookings")
    public ResponseEntity<List<com.example.Lankatools.entity.Booking>> getAllBookings(){
        List<com.example.Lankatools.entity.Booking> allBookings = bookingRepository.findAll();
        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("/stats")
    public ResponseEntity<java.util.Map<String, Long>> getDashboardStats() {
        java.util.Map<String, Long> stats = new java.util.HashMap<>();

        stats.put("totalTools", toolRepository.count());
        stats.put("totalBookings", bookingRepository.countByStatus(com.example.Lankatools.enums.BookingStatus.PENDING));
        stats.put("totalUsers", userRepository.count());

        long pendingToolsCount = toolService.getToolsByStatus(Toolstatus.PENDING).size();
        stats.put("pendingApprovals", pendingToolsCount);

        return ResponseEntity.ok(stats);
    }
}

