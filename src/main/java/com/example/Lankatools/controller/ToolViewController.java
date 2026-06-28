package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.repository.UserRepository;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
public class ToolViewController {

    @Autowired
    private ToolService toolService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/tools")
    public String viewCatalogPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        var tools = toolService.getToolsWithPagination(page, size, sortBy, direction);
        model.addAttribute("tools", tools);

        return "tools-list";
    }


    @GetMapping("/tools/detail/{id}")
    public String viewToolDetail(@PathVariable Long id, Model model) {
        Tool tool = toolService.getToolById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found with id: " + id));
        model.addAttribute("tool", tool);
        return "tool-detail";
    }


    @GetMapping("/tools/my-tools")
    public String viewMyTools(Model model) {
        User owner = getCurrentUser();
        List<Tool> myTools = toolService.getAllTools().stream()
                .filter(t -> t.getOwner() != null && t.getOwner().getId().equals(owner.getId()))
                .toList();
        model.addAttribute("myTools", myTools);
        return "my-tool";
    }


    @GetMapping("/tools/add")
    public String showAddToolForm(Model model) {
        model.addAttribute("tool", new Tool());
        return "tool-add";
    }


    @PostMapping("/tools/add")
    public String submitNewTool(@ModelAttribute Tool tool,
                                @RequestParam("imageFile") MultipartFile imageFile) {

        String savedImagePath = saveImageFile(imageFile);
        if (savedImagePath != null) {
            tool.setImageUrl(savedImagePath);
        }

        tool.setOwner(getCurrentUser());
        toolService.saveTool(tool);

        return "redirect:/tools/my-tools";
    }


    @GetMapping("/tools/edit/{id}")
    public String showEditToolForm(@PathVariable Long id, Model model) {
        Tool tool = toolService.getToolById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found with id: " + id));
        model.addAttribute("tool", tool);
        return "tool-add";
    }


    @PostMapping("/tools/edit/{id}")
    public String submitEditedTool(@PathVariable Long id,
                                   @ModelAttribute Tool tool,
                                   @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        if (imageFile != null && !imageFile.isEmpty()) {
            String savedImagePath = saveImageFile(imageFile);
            if (savedImagePath != null) {
                tool.setImageUrl(savedImagePath);
            }
        }

        toolService.updateTool(id, tool);

        return "redirect:/tools/my-tools";
    }


    @GetMapping("/tools/delete/{id}")
    public String deleteTool(@PathVariable Long id) {
        toolService.deleteTool(id);
        return "redirect:/tools/my-tools";
    }


    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found in database"));
    }

    private String saveImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/jpg", "image/png");
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new RuntimeException("Invalid image format. Only JPG and PNG are allowed.");
        }

        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new RuntimeException("Image file is too large. Maximum size is 5MB.");
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

            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
}