package com.example.newsapp.modules.article.service;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getLatestArticles() {
        return articleRepository.findTop20ByOrderByCreatedAtDesc();
    }

    public List<Article> getMostViewedArticles() {
        return articleRepository.findTop20ByOrderByViewCountDesc();
    }

    public List<Article> getArticlesByCategory(String category) {
        return articleRepository.findByCategoryOrderByCreatedAtDesc(category);
    }

    public List<Article> search(String keyword) {
        return articleRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Article getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article Not Found"));

        article.setViewCount(article.getViewCount() + 1);
        return articleRepository.save(article);
    }

    public Article createArticle(Article article) {
        // debug incoming article
        log.info("createArticle called: title='{}' slug='{}'", article == null ? null : article.getTitle(), article == null ? null : article.getSlug());

        // set created time if missing
        if (article.getCreatedAt() == null) {
            article.setCreatedAt(LocalDateTime.now());
        }

        // generate or normalize slug (use provided slug if present, otherwise generate from title)
        String sourceForSlug = (article.getSlug() != null && !article.getSlug().trim().isEmpty())
                ? article.getSlug()
                : (article.getTitle() == null ? "article" : article.getTitle());
        String base = slugify(sourceForSlug);
        String candidate = base;
        int suffix = 0;
        while (articleRepository.existsBySlug(candidate)) {
            suffix++;
            candidate = base + "-" + suffix;
        }
        article.setSlug(candidate);

        return articleRepository.save(article);
    }

  private String slugify(String input) {
    if (input == null) return "article";
    String nowhitespace = input.trim().toLowerCase();
    // remove accents
    String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
    String noAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    // replace non-alphanumeric with hyphen
    String slug = noAccents.replaceAll("[^a-z0-9]+", "-");
    // collapse hyphens
    slug = slug.replaceAll("-+", "-");
    // trim leading/trailing hyphens
    slug = slug.replaceAll("(^-+|-+$)", "");
    if (slug.isEmpty()) return "article";
    return slug;
  }
}
