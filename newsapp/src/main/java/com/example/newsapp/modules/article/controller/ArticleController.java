package com.example.newsapp.modules.article.controller;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*") // Hoặc cấu hình cụ thể domain frontend của Han
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    // ========== LẤY DANH SÁCH HIỂN THỊ TRANG CHỦ ==========
    @GetMapping
    public List<Article> getHomeArticles(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String category) {

        if ("all".equals(sort)) return articleService.getAllArticles();
        if ("newest".equals(sort)) return articleService.getLatestArticles();
        if ("most_viewed".equals(sort)) return articleService.getMostViewedArticles();
        if ("most_liked".equals(sort)) return articleService.getMostLikedArticles();

        if (category != null) return articleService.getArticlesByCategory(category);

        return articleService.getLatestArticles(); // mặc định
    }

    // ========== TÌM KIẾM ==========
    @GetMapping("/search")
    public List<Article> search(@RequestParam String q) {
        return articleService.search(q);
    }

    // ========== LẤY CHI TIẾT BÀI VIẾT ==========
    @GetMapping("/{value}")
    public ResponseEntity<Article> getArticle(@PathVariable String value) {
        Article article;
        // Nếu là số => tìm theo ID, ngược lại tìm theo slug
        if (value.matches("\\d+")) {
            long id = Long.parseLong(value);
            article = articleService.getArticleById(id);
        } else {
            article = articleService.getArticleBySlug(value);
        }
        return ResponseEntity.ok(article);
    }

    // ========== THÊM BÀI VIẾT (Yêu cầu đăng nhập) ==========
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article, java.security.Principal principal) {
        // Kiểm tra xem user có đăng nhập không
        if (principal == null) {
            log.warn("User chưa đăng nhập cố gắng đăng bài");
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }

        // 1. Lấy email từ token (Spring Security tự inject vào Principal)
        String email = principal.getName();
        log.info("User {} đang đăng bài: {}", email, article.getTitle());

        // 2. Gọi Service với đầy đủ 2 tham số: Article và Email tác giả
        Article saved = articleService.createArticle(article, email);

        return ResponseEntity.ok(saved);
    }

    // ========== THÔNG BÁO BÀI VIẾT MỚI ==========
    @GetMapping("/notifications/new")
    public List<Article> notifyNewArticles() {
        return articleService.getLatestArticles();
    }
}