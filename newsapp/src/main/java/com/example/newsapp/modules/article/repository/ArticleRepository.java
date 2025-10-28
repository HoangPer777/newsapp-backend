package com.example.newsapp.modules.article.repository;

import com.example.newsapp.modules.article.entity.Article;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query(value = """
      SELECT * FROM articles
      WHERE tsv @@ plainto_tsquery('simple', unaccent(:q))
      ORDER BY ts_rank_cd(tsv, plainto_tsquery('simple', unaccent(:q))) DESC
      """,
      countQuery = "SELECT count(*) FROM articles WHERE tsv @@ plainto_tsquery('simple', unaccent(:q))",
      nativeQuery = true)
  Page<Article> search(@Param("q") String q, Pageable pageable);
}
