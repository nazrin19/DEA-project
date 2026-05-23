package com.example.Lankatools.controller;

import com.example.Lankatools.entity.User;
import com.example.Lankatools.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 1. Serves the Login HTML Page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 2. Serves the Register HTML Page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }



    // 3. Handles the actual background Account Creation (API)
    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

}