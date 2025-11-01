package com.example.demo.security;

import com.example.demo.constant.SystemConstant;
import com.example.demo.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.security.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        // Generate JWT for this user and set it as HttpOnly cookie
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());
            String token = jwtTokenUtil.generateToken(userDetails);

            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    // .secure(true) // enable in production with HTTPS
                    .path("/")
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        } catch (Exception ex) {
            // If token generation fails, continue with redirect (don't block login)
        }

        if (response.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    public String determineTargetUrl(Authentication authentication) {
        if (authentication == null) {
            return SystemConstant.HOME;  // Will redirect to /trang-chu
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities == null
                ? List.of()
                : authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

        // First check for admin/manager roles, then staff
        if (isStaff(roles) || isAdmin(roles)) {
            return SystemConstant.ADMIN_HOME;  // This should be "/admin/home"
        }
        return SystemConstant.HOME;
    }

    private boolean isAdmin(List<String> roles) {
        if (roles == null) return false;
        return roles.contains(SystemConstant.MANAGER_ROLE)  // ROLE_MANAGER
            || roles.contains(SystemConstant.ADMIN_ROLE);   // ROLE_ADMIN
    }

    private boolean isStaff(List<String> roles) {
        return roles != null && roles.contains(SystemConstant.STAFF_ROLE);  // ROLE_STAFF
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}