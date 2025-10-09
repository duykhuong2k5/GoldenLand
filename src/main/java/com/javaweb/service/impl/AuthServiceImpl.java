package com.javaweb.service.impl;

import com.javaweb.entity.UserEntity;
import com.javaweb.model.request.LoginRequest;
import com.javaweb.model.request.PasswordResetRequest;
import com.javaweb.model.request.UserRegisterRequest;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.AuthService;
import com.javaweb.utils.OtpUtils;
import com.javaweb.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private OtpUtils otpUtils;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        // generate OTP and send
        String otp = otpUtils.generateOtp(request.getEmail());
        emailService.sendOtp(request.getEmail(), otp);
        // store the registration temporary data inside OTP store: password/fullName will be passed during verify
    }

    @Override
    @Transactional
    public void verifyOtp(String email, String otp, String password, String fullName) {
        if (!otpUtils.verifyOtp(email, otp)) throw new RuntimeException("Invalid or expired OTP");
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Email already exists");
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        userRepository.save(user);
        otpUtils.clearOtp(email);
    }

    @Override
    public String login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        return jwtTokenProvider.generateToken(user.getEmail());
    }

    @Override
    public void requestPasswordReset(String email) {
        if (!userRepository.existsByEmail(email)) throw new RuntimeException("Email not found");
        String otp = otpUtils.generateOtp(email);
        emailService.sendOtp(email, otp);
    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        if (!otpUtils.verifyOtp(request.getEmail(), request.getOtp())) throw new RuntimeException("Invalid or expired OTP");
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        otpUtils.clearOtp(request.getEmail());
    }
}
