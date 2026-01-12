package com.example.newsapp.modules.account.service;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;

// ko dùng nữa chuyển qua xác thực jwt
@Service
@RequiredArgsConstructor
public class AccountService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;

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

  public void sendResetPasswordEmail(String email) {
      User user = userRepository.findByEmail(email)
              .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

      // Tạo mã 6 số ngẫu nhiên
      String token = String.valueOf((int)((Math.random() * 899999) + 100000));
      user.setResetToken(token);
      user.setTokenExpiry(LocalDateTime.now().plusMinutes(15)); // Hết hạn sau 15p
      userRepository.save(user);

      // Gửi Mail
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(email);
      message.setSubject("Mã xác nhận đặt lại mật khẩu - NewsApp");
      message.setText("Mã xác nhận của bạn là: " + token + "\nMã có hiệu lực trong 15 phút.");
      mailSender.send(message);
    }

    @Transactional
    public void resetPassword(String email, String token, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        // Kiểm tra mã OTP
        if (user.getResetToken() == null || !user.getResetToken().equals(token)) {
            throw new RuntimeException("Mã xác nhận không đúng");
        }
        // Kiểm tra hết hạn
        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã xác nhận đã hết hạn");
        }

        // Đổi mật khẩu
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
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