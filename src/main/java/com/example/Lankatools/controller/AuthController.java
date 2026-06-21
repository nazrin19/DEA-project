package com.example.Lankatools.controller;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

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

    @GetMapping("/profile")
    public String profilePage(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }


    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

}