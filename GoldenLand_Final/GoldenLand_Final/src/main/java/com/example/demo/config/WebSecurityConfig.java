package com.example.demo.config;

import com.example.demo.security.CustomSuccessHandler;
import com.example.demo.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.security.jwt.JwtAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	@Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomSuccessHandler customSuccessHandler;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
                             CustomSuccessHandler customSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // ======= PUBLIC PATHS =======
                .requestMatchers(
                    "/", "/trang-chu", "/gioi-thieu", "/tin-tuc", "/lien-he","/api/public/**", "/san-pham",
                    "/register","/otp", "/auth/**","/vnpay_return","/ws/**", "/api/chat/**","/verify-otp",
                    "/css/**", "/js/**", "/images/**", "/fonts/**", "/assets/**","/vendor/**", "/webjars/**", "/favicon.ico",
                    "/chi-tiet-toa-nha/**",    // 🏢 Cho phép khách truy cập trang chi tiết
                    "/so-sanh" 
                ).permitAll()
             // ======= YÊU CẦU ĐĂNG NHẬP =======
                .requestMatchers("/web/**").hasAnyAuthority("ROLE_CUSTOMER", "STAFF", "MANAGER", "ADMIN")
                .requestMatchers("/customer/**", "/api/customer/**").authenticated()
                .requestMatchers("/forgot-password", "/reset-password").permitAll()
                
                //.requestMatchers(HttpMethod.GET, "/api/user/new").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/customer/new").hasAnyAuthority("MANAGER","ADMIN")

                .requestMatchers("/webjars/**").permitAll()
                // ======= WEBSOCKET / STOMP (YÊU CẦU ĐĂNG NHẬP) =======
                .requestMatchers(
                        "/ws/**",           // STOMP handshake
                        "/topic/**",        // broker pub/sub topics
                        "/queue/**",        // broker user queues (nếu dùng)
                        "/app/**"           // application destination prefix
                ).authenticated()       // 🔒 yêu cầu đã login
                .requestMatchers(HttpMethod.GET, "/api/public/market/price-history").permitAll()
             // ======= CORS Preflight (vẫn public để tránh lỗi OPTIONS 403) =======
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                .requestMatchers(HttpMethod.GET, "/api/review/**").permitAll()  // ai cũng xem được
                .requestMatchers(HttpMethod.POST, "/api/review/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/review/**").authenticated()

                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/buildings").permitAll()
                // ======= CUSTOMER API =======
                .requestMatchers("/api/customer/delete").hasAuthority("ADMIN")
                .requestMatchers("/api/customer/assignment").hasAuthority("MANAGER")
                .requestMatchers("/api/customer/**").hasAnyAuthority("STAFF", "MANAGER", "ADMIN")
                .requestMatchers("/customer/**").authenticated()

                // ======= ADMIN PAGES =======
                .requestMatchers("/admin/building-edit", "/admin/building-edit/**").hasAnyAuthority("MANAGER","ADMIN")
                .requestMatchers("/admin/building-list").hasAnyAuthority("STAFF","MANAGER","ADMIN")
                .requestMatchers("/admin/home").hasAnyAuthority("MANAGER", "STAFF", "ADMIN")
                .requestMatchers("/admin/customer-edit", "/admin/customer-edit-*").hasAnyAuthority("STAFF", "MANAGER","ADMIN")
                .requestMatchers("/admin/customer-list", "/admin/profile", "/admin/profile-password").hasAnyAuthority("STAFF", "MANAGER", "ADMIN")
                .requestMatchers("/admin/**").hasAnyAuthority("MANAGER", "ADMIN","STAFF")
                
                
                // ======= BUILDING =======
             // ❗ XÓA TOÀ NHÀ: CHỈ ADMIN
                .requestMatchers(HttpMethod.DELETE, "/api/building/*/delete").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/building/delete").hasAuthority("ADMIN")
                // (Tùy chọn an toàn) chặn mọi DELETE khác dưới /api/building/**
                .requestMatchers(HttpMethod.DELETE, "/api/building/**").hasAuthority("ADMIN")

                // Phân công: MANAGER hoặc ADMIN
                .requestMatchers(HttpMethod.POST, "/api/building/assignment").hasAnyAuthority("MANAGER","ADMIN")

                // Phần còn lại của /api/building/**
                .requestMatchers("/api/building/**").hasAnyAuthority("STAFF","MANAGER","ADMIN")

                // ======= EVERYTHING ELSE =======
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(customSuccessHandler)
                //.defaultSuccessUrl("/trang-chu", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
         // ====== JWT CONFIGURATION CHO API ======
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );
        
     // 🔥 Thêm JWT filter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
