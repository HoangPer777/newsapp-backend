package com.example.newsapp.modules.article.controller;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        if ("all".equals(sort))
            return articleService.getAllArticles();

        if ("newest".equals(sort))
            return articleService.getLatestArticles();

        if ("most_viewed".equals(sort))
            return articleService.getMostViewedArticles();

        if ("most_liked".equals(sort))
            return articleService.getMostLikedArticles();

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
    public Article createArticle(@RequestBody Article article) {
        log.info("ArticleController.createArticle received: title='{}' slug='{}' author={}",
                article == null ? null : article.getTitle(), article == null ? null : article.getSlug(),
                article == null ? null : article.getAuthor());
        return articleService.createArticle(article);
    }
    // ========== THÔNG BÁO BÀI VIẾT MỚI ==========
     @GetMapping("/notifications/new")
    public List<Article> notifyNewArticles() {
        return articleService.getLatestArticles();
    }
}
