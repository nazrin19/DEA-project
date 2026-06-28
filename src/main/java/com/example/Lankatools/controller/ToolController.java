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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        User owner = userRepository.findByEmail(currentUserEmail).orElse(null);
        if (owner == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated or found in database!");
        }
        tool.setOwner(owner);
        Tool savedTool = toolService.saveTool(tool);
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
    public ResponseEntity<Tool> updateStatus(@PathVariable Long id, @RequestParam Toolstatus status) { // 🌟 Matches Toolstatus
        Tool updatedTool = toolService.updateToolStatus(id, status);
        return ResponseEntity.ok(updatedTool);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty! Please select an image.");
        }

        String contentType = file.getContentType();
        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/jpg", "image/png");
        if (contentType == null || !allowedTypes.contains(contentType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid format! Only JPG, JPEG, and PNG are allowed.");
        }

        long maxSize = 2 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size is too large! Maximum Limit is 2MB.");
        }

        try {

            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = Paths.get(uploadDir, fileName).toString();
            file.transferTo(new File(filePath));

            return ResponseEntity.ok("/uploads/" + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed: " + e.getMessage());
        }
    }
}