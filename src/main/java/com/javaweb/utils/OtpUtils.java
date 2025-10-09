package com.javaweb.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpUtils {
    private static class OtpEntry { String otp; Instant expiresAt; }
    private final Map<String, OtpEntry> store = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String key) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        OtpEntry e = new OtpEntry();
        e.otp = otp;
        e.expiresAt = Instant.now().plusSeconds(5 * 60);
        store.put(key, e);
        return otp;
    }

    public boolean verifyOtp(String key, String otp) {
        OtpEntry e = store.get(key);
        if (e == null) return false;
        if (Instant.now().isAfter(e.expiresAt)) { store.remove(key); return false; }
        return e.otp.equals(otp);
    }

    public void clearOtp(String key) { store.remove(key); }
}
