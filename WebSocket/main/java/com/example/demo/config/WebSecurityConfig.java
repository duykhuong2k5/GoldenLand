package com.example.demo.config;

import com.example.demo.security.CustomSuccessHandler;
import com.example.demo.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.security.jwt.JwtAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;



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

                // ====== QUY ĐỊNH TRUY CẬP ======
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/", "/trang-chu", "/gioi-thieu", "/tin-tuc", "/lien-he", "/san-pham",
                                "/register","/otp", "/auth/**", "/vnpay_return", "/verify-otp", "/assets/**", "/vendor/**", "/webjars/**",
                                "/favicon.ico", "/chi-tiet-toa-nha/**"
                        ).permitAll()

                        // WebSocket (yêu cầu login)
                        .requestMatchers("/ws/**", "/topic/**", "/queue/**", "/app/**").authenticated()

                        // Chat API (yêu cầu login)
                        .requestMatchers("/api/chat/**", "/login").authenticated()

                        // CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Review API
                        .requestMatchers(HttpMethod.GET, "/api/review/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/review/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/review/**").authenticated()

                        // Customer API
                        .requestMatchers("/api/customer/delete").hasAuthority("MANAGER")
                        .requestMatchers("/api/customer/assignment").hasAuthority("MANAGER")
                        .requestMatchers("/api/customer/**").hasAnyAuthority("STAFF", "MANAGER", "ADMIN")

                        // Admin & Building
                        .requestMatchers("/admin/**").hasAnyAuthority("STAFF", "MANAGER", "ADMIN")
                        .requestMatchers("/api/building/**").hasAnyAuthority("STAFF","MANAGER","ADMIN")

                        // Mặc định: authenticated
                        .anyRequest().authenticated()
                )

                // ====== FORM LOGIN CHO WEB (admin/user) ======
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // ====== LOGOUT ======
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