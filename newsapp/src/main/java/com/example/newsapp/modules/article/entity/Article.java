package com.example.newsapp.modules.article.entity;

import com.example.newsapp.modules.account.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="articles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Article {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Long id;

  @Column(nullable=false) 
  private String title;

  @Column(columnDefinition = "TEXT", nullable=false)
  private String content;

  private String summary;
  
  // Category of the article (e.g. technology, sports)
  private String category;
  
  @Column(nullable=false, unique=true) 
  private String slug;

  @ManyToOne(optional=false) @JoinColumn(name="author_id") 
  private User author;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private int viewCount = 0;
  private int likeCount = 0;
}
