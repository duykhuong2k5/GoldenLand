package com.javaweb.service;

import com.javaweb.model.request.LoginRequest;
import com.javaweb.model.request.PasswordResetRequest;
import com.javaweb.model.request.UserRegisterRequest;

public interface AuthService {
    void register(UserRegisterRequest request);
    void verifyOtp(String email, String otp, String password, String fullName);
    String login(LoginRequest request);
    void requestPasswordReset(String email);
    void resetPassword(PasswordResetRequest request);
}
