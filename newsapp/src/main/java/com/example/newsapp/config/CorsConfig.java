package com.example.newsapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Cấu hình CORS để cho phép các yêu cầu từ các nguồn khác nhau
@Configuration
public class CorsConfig implements WebMvcConfigurer{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/**")
                .allowedOrigins("*") // Hoặc cụ thể "http://localhost:..."
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
        
}
