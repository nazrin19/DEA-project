package com.example.Lankatools.controller;

import com.example.Lankatools.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

            var tools = toolService.getToolsWithPagination(page, size, sortBy, direction);
            model.addAttribute("tools", tools);

            return "tools-list";
        }
    }

