package com.example.demo.security.utils;

import com.example.demo.model.dto.MyUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public final class SecurityUtils {

    private SecurityUtils() {}

    
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext() != null
                ? SecurityContextHolder.getContext().getAuthentication()
                : null;
    }

    
    public static MyUserDetail getPrincipal() {
        Authentication auth = getAuthentication();
        if (auth == null) return null;
        Object p = auth.getPrincipal();
        return (p instanceof MyUserDetail) ? (MyUserDetail) p : null;
    }

    
    public static String getUsername() {
        MyUserDetail my = getPrincipal();
        if (my != null) return my.getUsername();
        Authentication auth = getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }

    
    public static List<String> getAuthorities() {
        List<String> results = new ArrayList<>();
        Authentication auth = getAuthentication();
        if (auth == null) return results;
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null) return results;
        for (GrantedAuthority g : authorities) {
            results.add(g.getAuthority());
        }
        return results;
    }

    
    public static boolean hasAuthority(String authority) {
        return getAuthorities().contains(authority);
    }
}
