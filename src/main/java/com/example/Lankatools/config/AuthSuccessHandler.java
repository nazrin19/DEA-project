package com.example.Lankatools.controller; // Make sure this matches your project package structure

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component // Enables Spring to manage and inject this class into SecurityConfig
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        // Extracting all roles associated with the authenticated user profile
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        System.out.println("🚀 User logged in with roles: " + roles);

        // Routing engine matching roles to their specific application context paths
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_SHOP_OWNER")) {
            response.sendRedirect("/owner/dashboard"); // <-- Updated to /owner/dashboard
        } else if (roles.contains("ROLE_CUSTOMER")) {
            response.sendRedirect("/customer/dashboard");
        } else {
            // Safe fallback if user has basic authenticated access but no special role tier
            response.sendRedirect("/");
        }
    }
}