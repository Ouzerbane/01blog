package com._blog._blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // يجب إضافة هذا الاستيراد
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com._blog._blog.util.jwt.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. تفعيل CORS داخل سلسلة فلاتر Security
            .cors(Customizer.withDefaults()) 
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 2. السماح بمرور طلبات OPTIONS لجميع المسارات (مهم جداً لـ CORS Pre-flight)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 3. السماح بمرور مسارات التسجيل والدخول
                .requestMatchers("/regester", "/login").permitAll()
                // 4. طلب get-posts يتطلب التوثيق الآن (لتلقي الكوكي)
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}