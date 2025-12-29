package com.example.newsapp.modules.article.controller;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    // ========== LẤY DANH SÁCH HIỂN THỊ TRANG CHỦ ==========

    @GetMapping
    public List<Article> getHomeArticles(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String category) {

        if ("newest".equals(sort))
            return articleService.getLatestArticles();

        if ("most_viewed".equals(sort))
            return articleService.getMostViewedArticles();

        if (category != null)
            return articleService.getArticlesByCategory(category);

        return articleService.getLatestArticles(); // default
    }

    // ========== TÌM KIẾM ==========
    @GetMapping("/search")
    public List<Article> search(@RequestParam String q) {
        return articleService.search(q);
    }

@GetMapping("/{value}")
public ResponseEntity<Article> getArticle(@PathVariable String value) {

    Article article;

    // Nếu là số => tìm theo ID
    if (value.matches("\\d+")) {
        long id = Long.parseLong(value);
        article = articleService.getArticleById(id);
    }
    // Không phải số => tìm theo slug
    else {
        article = articleService.getArticleBySlug(value);
    }

    return ResponseEntity.ok(article);
}

    // ========== THÊM BÀI VIẾT (nếu có admin) ==========
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article, java.security.Principal principal) {
        // 1. Lấy email từ principal (người đang đăng nhập)
        String email = principal.getName();

        // 2. Truyền THÊM tham số email vào hàm createArticle để khớp với Service
        Article saved = articleService.createArticle(article, email);

        return ResponseEntity.ok(saved);
    }

}
