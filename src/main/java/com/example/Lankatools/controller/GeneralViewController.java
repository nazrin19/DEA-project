package com.example.Lankatools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralViewController {

    // 1. Catches the Shop Owner redirect after login
    @GetMapping("/shop/dashboard")
    public String showShopOwnerDashboard() {
        // Looks for src/main/resources/templates/shop/dashboard.html
        return "shop/dashboard";
    }

    // 2. Clear landing page for Customer views (if they have a dedicated dashboard)
    @GetMapping("/customer/dashboard")
    public String showCustomerDashboard() {
        // Looks for src/main/resources/templates/customer/dashboard.html
        return "customer/dashboard";
    }
}