package com.example.newsapp.modules.article.controller;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

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

    // ========== LẤY CHI TIẾT BÀI VIẾT ==========

    @GetMapping("/{id}")
    public Article getArticle(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    // ========== TÌM KIẾM ==========
    @GetMapping("/search")
    public List<Article> search(@RequestParam String q) {
        return articleService.search(q);
    }

    // ========== THÊM BÀI VIẾT (nếu có admin) ==========
    @PostMapping
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }
}
