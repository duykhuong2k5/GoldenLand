package com.javaweb.controller;

import com.javaweb.model.request.UserRegisterRequest;
import com.javaweb.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthViewController {
    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String fullName, Model model) {
        UserRegisterRequest req = new UserRegisterRequest();
        req.setEmail(email);
        req.setPassword(password);
        req.setFullName(fullName);
        try {
            authService.register(req);
            model.addAttribute("email", email);
            model.addAttribute("fullName", fullName);
            return "verify-otp";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyOtpView(@RequestParam(required = false) String email, @RequestParam(required = false) String fullName, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("fullName", fullName);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String doVerifyOtp(@RequestParam String email, @RequestParam String otp, @RequestParam String password, @RequestParam(required = false) String fullName, Model model) {
        try {
            authService.verifyOtp(email, otp, password, fullName);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("email", email);
            model.addAttribute("fullName", fullName);
            model.addAttribute("error", e.getMessage());
            return "verify-otp";
        }
    }
}
