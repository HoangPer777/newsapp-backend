package com.example.newsapp.modules.comment.controller;

import com.example.newsapp.modules.comment.entity.Comment;
import com.example.newsapp.modules.comment.service.CommentService;
import com.example.newsapp.modules.account.repository.UserRepository;
import com.example.newsapp.modules.account.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestBody CreateCommentRequest request, // Hứng JSON từ Flutter
            Authentication authentication // Lấy thông tin người đang đăng nhập
    ) {
        // 1. Kiểm tra đã đăng nhập chưa
        if (authentication == null) {
            return ResponseEntity.status(401).body("Vui lòng đăng nhập");
        }

        // 2. Lấy email từ Token
        String email = authentication.getName();

        // 3. Tìm User ID thật trong DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // 4. Gọi Service thêm comment
        // Lưu ý: userId lấy từ token, articleId lấy từ JSON
        Comment newComment = commentService.addComment(
                user.getId(),
                request.getArticleId(),
                request.getContent()
        );

        return ResponseEntity.ok(newComment);
    }

    @GetMapping("/{articleId}")
    public List<Comment> getComments(@PathVariable Long articleId) {
        return commentService.getComments(articleId);
    }
}

// Class nhỏ để hứng dữ liệu JSON
@Data
class CreateCommentRequest {
    private Long articleId;
    private String content;
}