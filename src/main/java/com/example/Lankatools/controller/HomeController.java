package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private ToolService toolService;

    @GetMapping("/")
    public String index(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        int pageSize = 6;
        Page<Tool> toolPage;
        String activeStatus = "APPROVED";

        boolean hasQuery = query != null && !query.trim().isEmpty();


        if (hasQuery) {
            toolPage = toolService.searchToolsByNameAndStatusPaginated(query.trim(), activeStatus, page, pageSize);
            model.addAttribute("searchQuery", query);
        } else {
            toolPage = toolService.getToolsByStatusWithPagination(activeStatus, page, pageSize, "id", "asc");
        }

        long totalAvailableTools = toolService.countToolsByStatus(activeStatus);

        model.addAttribute("tools", toolPage.getContent());
        model.addAttribute("totalTools", totalAvailableTools);
        model.addAttribute("activeToolsCount", toolPage.getNumberOfElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", toolPage.getTotalPages());

        return "index";
    }
}