package com.example.newsapp.modules.account.controller;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import com.example.newsapp.security.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController 
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtService jwt;

  @PostMapping("/register")
  public Map<String,String> register(@RequestBody RegisterReq req){
    if (users.existsByEmail(req.email)) throw new RuntimeException("Email exists");
    var user = new User();
    user.setEmail(req.email);
    user.setPasswordHash(encoder.encode(req.password));
    user.setDisplayName(req.displayName);
    user.setCreatedAt(java.time.Instant.now());
    user = users.save(user);
    String token = jwt.generateAccessToken(user.getEmail(), Map.of("uid", user.getId()));
    return Map.of("accessToken", token);
  }

  @PostMapping("/login")
  public Map<String,String> login(@RequestBody LoginReq req){
    authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.password));
    var u = users.findByEmail(req.email).orElseThrow();
    String token = jwt.generateAccessToken(u.getEmail(), Map.of("uid", u.getId()));
    return Map.of("accessToken", token);
  }

  @GetMapping("/me")
  public Map<String,Object> me(@RequestParam String email){
    var u = users.findByEmail(email).orElseThrow();
    return Map.of("id", u.getId(), "email", u.getEmail(), "displayName", u.getDisplayName());
  }

  @Data static class RegisterReq { @Email public String email; @NotBlank public String password; public String displayName; }
  @Data static class LoginReq { @Email public String email; @NotBlank public String password; }
}
