package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.UserRepository;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<Tool>> getPaginatedTools(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<Tool> tools = toolService.getToolsWithPagination(page, size, sortBy, direction);
        return ResponseEntity.ok(tools);
    }

    @PostMapping
    public ResponseEntity<?> saveTool(@RequestBody Tool tool) {
    /**
     * Unified Tool Registration Gateway
     * Accepts text properties alongside binary image streams simultaneously.
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveTool(@RequestParam("name") String name,
                                      @RequestParam("category") String category,
                                      @RequestParam("dailyRate") Double dailyRate,
                                      @RequestParam("description") String description,
                                      @RequestParam(value = "file", required = false) MultipartFile file) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        User owner = userRepository.findByEmail(currentUserEmail).orElse(null);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated or found in database!");
        }

        // Initialize core entity properties
        Tool tool = new Tool();
        tool.setName(name);
        tool.setCategory(category);
        tool.setDailyRate(dailyRate);
        tool.setDescription(description);
        tool.setOwner(owner);

        // 🛡️ CRITICAL: New tools default to PENDING until the admin approves them
        tool.setStatus(Toolstatus.PENDING);

        // Process File Attachment if present
        if (file != null && !file.isEmpty()) {
            // Format Content Validations
            String contentType = file.getContentType();
            List<String> allowedTypes = Arrays.asList("image/jpeg", "image/jpg", "image/png");
            if (contentType == null || !allowedTypes.contains(contentType)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid format! Only JPG, JPEG, and PNG are allowed.");
            }

            // Size Constraint Validations (2MB Limit)
            long maxSize = 2 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size is too large! Maximum Limit is 2MB.");
            }

            try {
                // Determine absolute write target location relative to your execution folder
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Append millisecond timestamps to safeguard file names against overriding duplication
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String filePath = Paths.get(uploadDir, fileName).toString();
                file.transferTo(new File(filePath));

                // Assign the web access URL context to your entity
                tool.setImageUrl("/uploads/" + fileName);

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image storage mapping failed: " + e.getMessage());
            }
        } else {
            // Fallback placeholder asset context if no image was selected
            tool.setImageUrl("/uploads/default-placeholder.png");
        }

        Tool savedTool = toolService.saveTool(tool);
        System.out.println("💾 Pending tool record cataloged successfully for review: " + savedTool.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTool);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tool> updateTool(@PathVariable Long id, @RequestBody Tool tool) {
        Tool updated = toolService.updateTool(id, tool);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTool(@PathVariable Long id) {
        toolService.deleteTool(id);
        return ResponseEntity.ok("Tool deleted successfully");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Tool> updateStatus(@PathVariable Long id, @RequestParam Toolstatus status) {
        Tool updatedTool = toolService.updateToolStatus(id, status);
        return ResponseEntity.ok(updatedTool);
    }
}