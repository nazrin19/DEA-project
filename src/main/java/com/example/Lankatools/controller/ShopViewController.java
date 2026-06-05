
package com.example.Lankatools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopViewController {

    // Shop Owner home panel showing their own tool listings
    @GetMapping("/dashboard")
    public String showShopDashboard() {
        return "shop/dashboard"; // Maps to templates/shop/dashboard.html
    }

    // Specific page where shop owners can post a new tool for approval
    @GetMapping("/upload-tool")
    public String showUploadToolForm() {
        return "shop/upload-tool"; // Maps to templates/shop/upload-tool.html
    }
}