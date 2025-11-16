package com.example.newsapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter;
  private final UserDetailsService userDetailsService;

  @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean AuthenticationManager authManager() {
    DaoAuthenticationProvider p = new DaoAuthenticationProvider();
    p.setPasswordEncoder(passwordEncoder());
    p.setUserDetailsService(userDetailsService);
    return new ProviderManager(p);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
          // Cho phép public bắt buộc
          .requestMatchers(
              "/actuator/**",
              "/v3/api-docs/**",
              "/swagger-ui/**",
              "/swagger-ui.html",
              "/auth/**"
          ).permitAll()

          // Cho phép article GET không cần login
          // Note: include both "/api/articles" and "/api/articles/**" because
          // "/api/articles/**" may not match the exact "/api/articles" path depending
          // on matcher behaviour; explicitly listing both avoids 403 for root list.
          .requestMatchers(HttpMethod.GET, "/api/articles", "/api/articles/**").permitAll()

          // Nhưng POST /api/articles phải có JWT
          .requestMatchers(HttpMethod.POST, "/api/articles").authenticated()

          // Các route khác yêu cầu JWT
          .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .cors(Customizer.withDefaults());

    return http.build();
  }
}