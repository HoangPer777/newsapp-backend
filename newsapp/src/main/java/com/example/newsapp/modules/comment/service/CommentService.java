package com.example.newsapp.modules.comment.service;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.repository.ArticleRepository;
import com.example.newsapp.modules.comment.entity.Comment;
import com.example.newsapp.modules.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public Comment addComment(Long userId, Long articleId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setArticle(article);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId);
    }
}
