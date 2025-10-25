package com.example.demo.config;

import com.example.demo.security.CustomSuccessHandler; // vẫn giữ như file 1
import com.example.demo.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

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
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // ======= PUBLIC PATHS =======
                        // === [REPLACED with File 2]
                        .requestMatchers(
                                "/register", "/otp",
                                "/auth/register", "/auth/verify-otp", "/auth/otp/send/email",
                                "/", "/trang-chu", "/gioi-thieu", "/tin-tuc", "/lien-he", "/san-pham",
                                "/css/**", "/js/**", "/images/**", "/assets/**", "/webjars/**", "/favicon.ico", "/error",
                                "/login", "/vnpay_return", "/web/**"
                        ).permitAll()
                        // === [END REPLACED]

                        // ======= CUSTOMER API =======
                        // === [REPLACED with File 2]
                        .requestMatchers("/customer/**", "/api/customer/**").authenticated()
                        // === [END REPLACED]

                        // ======= ADMIN PAGES =======
                        // === [REPLACED with File 2 simplified authority logic ===
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN","MANAGER","STAFF")
                        // === [END REPLACED]

                        // ======= EVERYTHING ELSE =======
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        // === [REPLACED with File 2: bỏ CustomSuccessHandler ===
                        // .successHandler(customSuccessHandler)
                        .defaultSuccessUrl("/trang-chu", true)
                        // === [END REPLACED]
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
