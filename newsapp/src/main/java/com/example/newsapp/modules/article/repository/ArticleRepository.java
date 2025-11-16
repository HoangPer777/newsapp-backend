package com.example.newsapp.modules.article.repository;

import com.example.newsapp.modules.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  List<Article> findTop20ByOrderByCreatedAtDesc();

  List<Article> findTop20ByOrderByViewCountDesc();

  List<Article> findByCategoryOrderByCreatedAtDesc(String category);

  List<Article> findByTitleContainingIgnoreCase(String keyword);
  
}
