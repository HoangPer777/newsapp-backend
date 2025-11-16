package com.example.newsapp.modules.account.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
@Getter
@NoArgsConstructor
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true)
  private String email;

  @Column(nullable=false, name="password_hash")
  private String passwordHash;

  private String displayName;

  @Column(nullable=false, name="created_at", updatable=false)
  private LocalDateTime createdAt;

  // Constructor with createdAt
  public User(String email, String passwordHash, String displayName, LocalDateTime createdAt) {
    this.email = email;
    this.passwordHash = passwordHash;
    this.displayName = displayName;
    this.createdAt = createdAt;
  }

  public void setEmail(String email) { this.email = email; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public void setDisplayName(String displayName) { this.displayName = displayName; }
}