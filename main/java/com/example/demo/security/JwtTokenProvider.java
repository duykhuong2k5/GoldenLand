package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Sinh và xác thực JWT (HS256).
 * Lưu ý: jwt.secret phải dài >= 32 bytes.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessExpMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-exp-ms:3600000}") long accessExpMs) {
        // Bảo đảm đủ độ dài cho HS256
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMs = accessExpMs;
    }

    /** Tạo Access Token chứa username + roles */
    public String generateToken(String username, List<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessExpMs);
        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("roles", roles))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Lấy username từ token (ném lỗi nếu token không hợp lệ) */
    public String getUsername(String token) {
        return parse(token).getBody().getSubject();
    }

    /** Lấy danh sách vai trò từ token */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Object r = parse(token).getBody().get("roles");
        return (List<String>) r;
    }

    /** Kiểm tra token hợp lệ */
    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
