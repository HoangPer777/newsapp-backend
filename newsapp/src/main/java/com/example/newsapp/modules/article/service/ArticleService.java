package com.example.newsapp.modules.article.service;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

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
        return articleRepository.save(article);
    }
}
