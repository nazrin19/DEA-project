package com.example.Lankatools.controller;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            // Processes text properties and secures password encryption inside the service layer
            userService.registerUser(user);

            // Passes a flash attribute notification message to be read smoothly on your login template page
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please log in.");
            return "redirect:/login";

        } catch (Exception e) {
            // Appends tracking parameters (?error) to automatically trigger your register.html error banner layout
            return "redirect:/register?error";
        }
    }
}