package com.example.Lankatools.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(Model model) {
        model.addAttribute("message", "Page not found.");
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handle403(Model model) {
        model.addAttribute("message", "You don't have permission.");
        return "error/403";
    }

    @ExceptionHandler(Exception.class)
    public String handle500(Exception ex, Model model) {
        model.addAttribute("message", "Something went wrong.");
        return "error/500";
    }
}
