package com.example.newsapp.modules.account.controller;

import com.example.newsapp.modules.account.controller.AuthController.LoginReq;
import com.example.newsapp.modules.account.controller.AuthController.RegisterReq;
import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import com.example.newsapp.security.JwtService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.example.newsapp.modules.account.service.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final AccountService accountService;
  private final JwtService jwt;

  @PostMapping("/register")
  public Map<String, String> register(@RequestBody @jakarta.validation.Valid RegisterReq req) {
    User user = accountService.registerUser(req.email, req.password, req.displayName);
    String token = jwt.generateAccessToken(user.getEmail(), Map.of("uid", user.getId()));
    return Map.of("accessToken", token);
  }

//  @PostMapping("/login")
//  public Map<String, Object> login(@RequestBody LoginReq req) {
//    User user = accountService.loginUser(req.email, req.password);
//    String token = jwt.generateAccessToken(user.getEmail(), Map.of("uid", user.getId()));
//
//    Map<String, Object> response = new HashMap<>();
//    response.put("accessToken", token);
//    response.put("userId", user.getId()); // Trả về ID để Flutter có cái mà gọi /me?uid=...
//    return response;
//  }
@PostMapping("/login")
public Map<String, Object> login(@RequestBody LoginReq req) {
    User user = accountService.loginUser(req.email, req.password);

    // Thêm Role vào Claims của JWT
    Map<String, Object> claims = new HashMap<>();
    claims.put("uid", user.getId());
    claims.put("role", user.getRole().name()); // Thêm dòng này

    String token = jwt.generateAccessToken(user.getEmail(), claims);

    Map<String, Object> response = new HashMap<>();
    response.put("accessToken", token);
    response.put("userId", user.getId());
    response.put("role", user.getRole().name()); // Trả về cho Flutter để hiện UI Admin
    return response;
}


  @GetMapping("/me")
  public Map<String, Object> me(@RequestParam Long uid) {
    User u = users.findById(uid)
                  .orElseThrow(() -> new RuntimeException("User not found with id: " + uid));

    Map<String, Object> response = new HashMap<>();
    response.put("id", u.getId());
    response.put("email", u.getEmail());
    response.put("displayName", u.getDisplayName());
    response.put("phoneNumber", u.getPhoneNumber()); 
    response.put("gender", u.getGender());
    response.put("address", u.getAddress());
    response.put("avatarUrl", u.getAvatarUrl());
    
    return response;
  }

  @PutMapping("/update")
  public Map<String, Object> updateUserInfo(@RequestParam Long uid, @RequestBody UpdateUserReq req) {
    // 1. Gọi Service để cập nhật (Đảm bảo truyền đúng phoneNumber và address)
    User updatedUser = accountService.updateUserInfo(
        uid, 
        req.displayName, 
        req.phoneNumber, 
        req.gender, 
        req.address
    );

    // 2. Trả về thông tin đã cập nhật bằng HashMap để an toàn với giá trị null
    Map<String, Object> response = new HashMap<>();
    response.put("id", updatedUser.getId());
    response.put("email", updatedUser.getEmail());
    response.put("displayName", updatedUser.getDisplayName());
    response.put("phoneNumber", updatedUser.getPhoneNumber());
    response.put("gender", updatedUser.getGender());
    response.put("address", updatedUser.getAddress());
    response.put("avatarUrl", updatedUser.getAvatarUrl());
    
    return response;
  }

  @PostMapping("/change-password")
  public Map<String, String> changePassword(
        @RequestParam Long uid,
        @RequestBody ChangePasswordReq req
  ) {
    User user = users.findById(uid)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 1. Kiểm tra mật khẩu cũ (Dùng PasswordEncoder để so khớp)
    if (!encoder.matches(req.oldPassword, user.getPasswordHash())) {
        throw new RuntimeException("Old password is incorrect");
    }

    // 2. Mã hóa mật khẩu mới và cập nhật
    user.setPasswordHash(encoder.encode(req.newPassword));
    
    // 3. Cập nhật thời gian thay đổi (Đồng bộ với Entity User mới)
    user.setUpdatedAt(LocalDateTime.now());
    
    users.save(user);

    return Map.of("message", "Password updated successfully");
  }



  @Data
  static class RegisterReq {
    @Email
    public String email;
    @NotBlank
    public String password;
    public String displayName;
  }

  @Data
  static class LoginReq {
    @Email
    public String email;
    @NotBlank
    public String password;
  }
  @Data static class UpdateUserReq {
    public String displayName;
    public String phoneNumber; 
    public String gender;
    public String address;
  }
  @Data
  static class ChangePasswordReq {
    public String oldPassword;
    public String newPassword;
  }

}
