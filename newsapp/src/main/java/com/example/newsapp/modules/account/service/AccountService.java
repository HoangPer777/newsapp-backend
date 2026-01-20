package com.example.newsapp.modules.account.service;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.Collections;
import com.example.newsapp.modules.account.entity.Role;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Map; 


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
      System.out.println("MÃ HASH CHUẨN TRÊN MÁY HAN: " + passwordEncoder.encode("123456"));
    var user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

      System.out.println("Mật khẩu từ Flutter: " + password);
      System.out.println("Mật khẩu băm trong DB: " + user.getPasswordHash());

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new RuntimeException("Invalid password");
    }

    return user;
  }

  public User processFacebookLogin(String fbToken) {
        // 1. Gọi Graph API của Facebook để lấy thông tin user
        String fbUrl = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + fbToken;
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> fbRes = restTemplate.getForObject(fbUrl, Map.class);

        if (fbRes != null && fbRes.get("email") != null) {
            String email = fbRes.get("email");
            String name = fbRes.get("name");

            // 2. Tìm hoặc tạo mới User
            return userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User(email, "FB_AUTH", name, LocalDateTime.now());
                newUser.setRole(Role.USER);
                newUser.setStatus("ACTIVE");
                return userRepository.save(newUser);
            });
        } else {
            throw new RuntimeException("Xác thực Facebook thất bại");
        }
    }
  private final String GOOGLE_CLIENT_ID = "7797438524-ull6k9kur4dhlv993sg23iod0pnblqik.apps.googleusercontent.com";

  public User processGoogleLogin(String idTokenString) throws Exception {
    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
            .build();

    GoogleIdToken idToken = verifier.verify(idTokenString);
    if (idToken != null) {
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        return userRepository.findByEmail(email).orElseGet(() -> {
            // Nếu chưa có thì tạo mới user
            User newUser = new User(email, "GOOGLE_AUTH", name, LocalDateTime.now());
            newUser.setRole(Role.USER);
            newUser.setStatus("ACTIVE");
            return userRepository.save(newUser);
        });
    } else {
        throw new RuntimeException("Xác thực Google thất bại");
    }
  }

  public void sendResetPasswordEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

    // Tạo mã 6 số ngẫu nhiên
    String token = String.valueOf((int) ((Math.random() * 899999) + 100000));
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
  public User updateUserInfo(Long userId, String newDisplayName, String newPhoneNumber, String newGender,
      String newAddress) {

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

    // Nhờ @Transactional, thay đổi sẽ tự động được lưu xuống Database khi kết thúc
    // hàm
    return user;
  }
}