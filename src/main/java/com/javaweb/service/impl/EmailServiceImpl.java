package com.javaweb.service.impl;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {
    // Mock email service: in real app inject JavaMailSender
    public void sendOtp(String to, String otp) {
        System.out.println("[MOCK EMAIL] Sending OTP to: " + to + " OTP=" + otp);
    }
}
