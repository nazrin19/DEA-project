package com.example.Lankatools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralViewController {


    @GetMapping("/customer/dashboard")
    public String showCustomerDashboard() {
        return "customer/dashboard";
    }

    @GetMapping("/owner/dashboard")
    public String showOwnerDashboard() {
        return "owner/dashboard";
    }
}