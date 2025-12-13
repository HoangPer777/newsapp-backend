package com.example.newsapp.modules.comment.entity;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.article.entity.Article;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"passwordHash", "role", "createdAt", "updatedAt", "comments"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @JsonIgnore
    private Article article;
}
