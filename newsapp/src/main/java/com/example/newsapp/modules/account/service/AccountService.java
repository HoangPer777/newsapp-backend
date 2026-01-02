package com.example.newsapp.modules.account.service;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

// ko dùng nữa chuyển qua xác thực jwt
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
        LocalDateTime.now());
    user.setRole(com.example.newsapp.modules.account.entity.Role.USER);
    user.setStatus("ACTIVE");
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
  @Transactional
  public User updateUserInfo(Long userId, String newDisplayName, String newPhoneNumber, String newGender, String newAddress) {
        
    // 1. Tìm người dùng theo ID
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new RuntimeException("User not found"));

    // 2. Cập nhật các trường (Chỉ cập nhật nếu giá trị gửi lên không null)
    if (newDisplayName != null) {
      user.setDisplayName(newDisplayName);
    }
    if (newPhoneNumber != null) {
      user.setPhoneNumber(newPhoneNumber);
    }
    if (newGender != null) {
      user.setGender(newGender);
    }
    if (newAddress != null) {
      user.setAddress(newAddress);
    }

    // 3. Cập nhật thời gian chỉnh sửa cuối cùng
    user.setUpdatedAt(LocalDateTime.now());

    // Nhờ @Transactional, thay đổi sẽ tự động được lưu xuống Database khi kết thúc hàm
    return user;
  }
}