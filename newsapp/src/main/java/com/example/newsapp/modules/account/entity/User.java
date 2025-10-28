package com.example.newsapp.modules.account.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Long id;
  
  @Column(nullable=false, unique=true) 
  private String email;

  @Column(nullable=false, name="password_hash") 
  private String passwordHash;

  private String displayName;
  @Column(nullable=false) private Instant createdAt = Instant.now();
}
