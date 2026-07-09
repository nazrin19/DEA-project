package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ToolViewController {

    @Autowired
    private ToolService toolService;


    @GetMapping("/tools")
    public String viewCatalogPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Page<Tool> toolsPage = toolService.getToolsWithPagination(page, size, sortBy, direction);


        model.addAttribute("tools", toolsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", toolsPage.getTotalPages());


        return "tools";
    }


    @GetMapping("/tools/detail/{id}")
    public String showToolDetail(@PathVariable("id") Long id, Model model) {
        // Safe unpacking using .orElseThrow() to match the Service's Optional wrapper
        Tool tool = toolService.getToolById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool Id: " + id));

        model.addAttribute("tool", tool);

        return "detail"; // Renders src/main/resources/templates/detail.html
    }
}