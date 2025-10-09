package com.javaweb.controller.web.api;

import com.javaweb.model.request.LoginRequest;
import com.javaweb.model.request.PasswordResetRequest;
import com.javaweb.model.request.UserRegisterRequest;
import com.javaweb.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthAPI {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody UserRegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("OTP sent to email. Verify to complete registration.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp, @RequestParam String password, @RequestParam(required = false) String fullName) {
        authService.verifyOtp(email, otp, password, fullName);
        return ResponseEntity.ok("Registration complete");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/password/request")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok("OTP sent for password reset");
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> reset(@Validated @RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password updated");
    }
}
