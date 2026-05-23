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
@RequestMapping("/admin/api")

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

    @GetMapping("/bookings")
    public ResponseEntity<List<com.example.Lankatools.entity.Booking>> getAllBookings(){
        List<com.example.Lankatools.entity.Booking> allBookings = bookingRepository.findAll();
        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("/stats")
    public ResponseEntity<com.example.Lankatools.dto.AdminStatsDto> getDashboardStats() {

        long totalUsers = userRepository.count();
        long totalTools = toolRepository.count();
        long totalBookings = bookingRepository.count(); // Uses inherited JPA count()
        long pendingApprovals = toolService.getToolsByStatus(Toolstatus.PENDING).size();

        com.example.Lankatools.dto.AdminStatsDto stats = new com.example.Lankatools.dto.AdminStatsDto(
                totalUsers,
                totalTools,
                totalBookings,
                pendingApprovals
        );

        return ResponseEntity.ok(stats);
    }

}

