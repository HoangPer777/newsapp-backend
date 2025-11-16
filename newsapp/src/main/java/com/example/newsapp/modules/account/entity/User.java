package com.example.newsapp.modules.account.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="users")
@Getter 
@Setter 
@NoArgsConstructor 
@Builder
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Long id;
  
  @Column(nullable=false, unique=true) 
  private String email;

  @Column(nullable=false, name="password_hash") 
  private String passwordHash;

  private String displayName;
  
  @Column(nullable=false)
  private Instant createdAt;

  public User(Long id, String email, String passwordHash, String displayName, Instant createdAt) {
    this.id = id;
    this.email = email;
    this.passwordHash = passwordHash;
    this.displayName = displayName;
    this.createdAt = createdAt;
  }

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }
}
