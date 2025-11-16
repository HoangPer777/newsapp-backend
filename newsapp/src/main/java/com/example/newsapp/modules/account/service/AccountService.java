package com.example.newsapp.modules.account.service;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User registerUser(String email, String password, String displayName) {
    if (userRepository.existsByEmail(email)) {
      throw new RuntimeException("Email already exists");
    }
    User user = new User(
            email,
            passwordEncoder.encode(password),
            displayName,
            LocalDateTime.now()
    );
    return userRepository.save(user);
  }

  public User loginUser(String email, String password) {
    var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new RuntimeException("Invalid password");
    }

    return user;
  }
}