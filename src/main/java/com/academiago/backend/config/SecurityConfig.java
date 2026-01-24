package com.academiago.backend.config;

import com.academiago.backend.security.JwtFilter;
import com.academiago.backend.security.JwtUtil;
import com.academiago.backend.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // ADMIN has full access
                        .requestMatchers("/api/**").hasRole("ADMIN")

                        // TEACHER access (teaching-related + content management)
                        .requestMatchers("/api/subjects/**", "/api/students/**",
                                "/api/programs/**", "/api/semesters/**",
                                "/api/assignments/**", "/api/notices/**",
                                "/api/answers/**", "/api/course-material/**")
                        .hasAnyRole("TEACHER", "ADMIN")

                        // STUDENT access (view-only endpoints)
                        .requestMatchers(HttpMethod.GET, "/api/subjects/**", "/api/semesters/**",
                                "/api/programs/**", "/api/student-results/**",
                                "/api/assignments/**", "/api/notices/**",
                                "/api/course-material/**")
                        .hasAnyRole("STUDENT", "TEACHER", "ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
