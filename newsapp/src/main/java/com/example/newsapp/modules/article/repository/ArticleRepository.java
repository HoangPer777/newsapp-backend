package com.example.newsapp.modules.article.repository;

import com.example.newsapp.modules.article.entity.Article;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query(value = """
      SELECT * FROM articles
      WHERE tsv @@ plainto_tsquery('simple', unaccent(:q))
      ORDER BY ts_rank_cd(tsv, plainto_tsquery('simple', unaccent(:q))) DESC
      """,
      countQuery = "SELECT count(*) FROM articles WHERE tsv @@ plainto_tsquery('simple', unaccent(:q))",
      nativeQuery = true)
  Page<Article> search(@Param("q") String q, Pageable pageable);

    Optional<Article> findBySlug(String slug);

    List<Article> findTop20ByOrderByCreatedAtDesc();

    List<Article> findTop20ByOrderByViewCountDesc();

    List<Article> findByCategoryOrderByCreatedAtDesc(String category);

    List<Article> findByTitleContainingIgnoreCase(String keyword);

    List<Article> findTop4ByCategoryAndSlugIsNotOrderByCreatedAtDesc(String category, String slug, Pageable pageable);

// 3. Cho các phương thức phân trang cơ bản
// Nếu bạn muốn dùng PageRequest trong Controller, bạn cần phương thức này
// public Page<Article> findAll(Pageable pageable); // (đã có từ JpaRepository)
// public Page<Article> search(String q, Pageable pageable); // (cần custom query)
}
