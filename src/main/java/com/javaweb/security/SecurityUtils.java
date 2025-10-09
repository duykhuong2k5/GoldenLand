package com.javaweb.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    public String encodePassword(String raw) {
        return new BCryptPasswordEncoder().encode(raw);
    }
}
