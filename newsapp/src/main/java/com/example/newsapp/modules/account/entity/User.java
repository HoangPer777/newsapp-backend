package com.example.newsapp.modules.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true)
  private String email;

  @Column(nullable=false, name="password_hash")
  @JsonIgnore
  private String passwordHash;

  private String displayName;

  @Column(nullable=false, name="created_at", updatable=false)
  private LocalDateTime createdAt;

  @Column(name="phone_number")
  private String phoneNumber;

   private String gender;

  private String address;

  @Column(name="avatar_url")
  private String avatarUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false)
  private Role role = Role.USER;

  private String status = "ACTIVE";

  @Column(name="updated_at")
  private LocalDateTime updatedAt;

  // Constructor with createdAt
  public User(String email, String passwordHash, String displayName, LocalDateTime createdAt) {
    this.email = email;
    this.passwordHash = passwordHash;
    this.displayName = displayName;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
  }

  
}