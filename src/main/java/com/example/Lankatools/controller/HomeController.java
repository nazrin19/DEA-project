package com.example.Lankatools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.service.ToolService;

@Controller
public class HomeController {

    @Autowired
    private ToolService toolService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tools", toolService.getToolsByStatus(Toolstatus.APPROVED));
        return "index";
    }

    @GetMapping("/403")
    public String forbidden() {
        return "error/403";
    }
}
