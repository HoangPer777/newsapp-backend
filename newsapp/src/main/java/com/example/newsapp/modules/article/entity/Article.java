package com.example.newsapp.modules.article.entity;

import com.example.newsapp.modules.account.entity.User;
// import com.example.newsapp.modules.author.entity.Author; // Deleted
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(columnDefinition = "TEXT")
  private String summary;

  private String category;

  @Column(nullable = false, unique = true)
  private String slug;

  @ManyToOne(optional = false)
  @JoinColumn(name = "author_id")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "passwordHash", "role", "status" }) // avoid exposing
                                                                                                     // sensitive user
                                                                                                     // data
  private User author;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private int viewCount = 0;
  private int likeCount = 0;

  // hinh anh
  @Column(name = "image_url")
  private String imageUrl;

  // private Instant publishedAt;
}
