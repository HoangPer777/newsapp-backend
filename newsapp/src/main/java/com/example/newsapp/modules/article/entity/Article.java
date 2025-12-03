package com.example.newsapp.modules.article.entity;

import com.example.newsapp.modules.account.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="articles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Article {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String title;
  @Column(nullable=false, unique=true) private String slug;
  @ManyToOne(optional=false) @JoinColumn(name="author_id") private User author;
  @Column(nullable=false, columnDefinition="text") private String contentPlain;
  //hinh anh
  @Column(name = "image_url") // Khớp tên cột trong DB
  private String imageUrl;
  @Column(name = "view_count")
  private Long viewCount = 0L;
  private String category;
  private Instant publishedAt;
}
