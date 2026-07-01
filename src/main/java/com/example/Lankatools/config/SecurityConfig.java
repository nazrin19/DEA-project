package com.example.Lankatools.config;

import com.example.Lankatools.service.CustomUserDetailsService;
import com.example.Lankatools.config.AuthSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthSuccessHandler authSuccessHandler;


    public SecurityConfig(CustomUserDetailsService customUserDetailsService, AuthSuccessHandler authSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(customUserDetailsService)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/tools", "/tools/detail/**", "/login", "/register",
                                "/api/auth/**",
                                "/css/**", "/js/**", "/images/**",
                                "/uploads/**", "/static/**"
                        ).permitAll()


                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/owner/**").hasRole("SHOP_OWNER") // <-- Updated to /owner/**
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")


                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // Reference the Spring-managed bean instance here
                        .successHandler(authSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}