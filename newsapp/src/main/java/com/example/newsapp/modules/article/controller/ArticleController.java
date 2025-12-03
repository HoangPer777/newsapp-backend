package com.example.newsapp.modules.article.controller;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
  private final ArticleRepository repo;

  @GetMapping public Object list(@RequestParam(defaultValue="0") int page,
                                 @RequestParam(defaultValue="10") int size){
    return repo.findAll(PageRequest.of(page, size));
  }

 // @GetMapping("/{id}") public Object get(@PathVariable Long id){ return repo.findById(id).orElseThrow(); }
@GetMapping("/{value}")
public Object get(@PathVariable String value) {
    try {
        Long id = Long.parseLong(value);
        return repo.findById(id).orElseThrow();
    } catch (NumberFormatException e) {
        return repo.findBySlug(value).orElseThrow();
    }
}
//    // ========== THÊM BÀI VIẾT (nếu có admin) ==========
//    @PostMapping
//    public Article createArticle(@RequestBody Article article) {
//        return articleService.createArticle(article);
//    }

  @GetMapping("/search")
  public Object search(@RequestParam String q,
                       @RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size) {
    return repo.search(q, PageRequest.of(page, size));
  }

}
//package com.example.newsapp.modules.article.controller;
//
//import com.example.newsapp.modules.article.service.ArticleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/articles")
//@RequiredArgsConstructor // Dùng để tiêm ArticleService
//public class ArticleController {
//
//    // ⭐ Đã khai báo final để @RequiredArgsConstructor hoạt động
//    private final ArticleService articleService;
//
//    @GetMapping
//    public Object list(@RequestParam(defaultValue="0") int page,
//                       @RequestParam(defaultValue="10") int size){
//        // Giả định ArticleService có phương thức findAll
//        return articleService.findAll(PageRequest.of(page, size));
//    }
//
//    @GetMapping("/{value}")
//    public Object get(@PathVariable String value) {
//        try {
//            Long id = Long.parseLong(value);
//            // Lấy theo ID, Service sẽ tăng View Count
//            return articleService.getArticleById(id);
//        } catch (NumberFormatException e) {
//            // Lấy theo Slug, Service sẽ tăng View Count
//            return articleService.getArticleDetailBySlug(value);
//        }
//    }
//
//    @GetMapping("/search")
//    public Object search(@RequestParam String q,
//                         @RequestParam(defaultValue="0") int page,
//                         @RequestParam(defaultValue="10") int size) {
//        // Giả định ArticleService có phương thức search
//        return articleService.search(q, PageRequest.of(page, size));
//    }
//
//    // ⭐ ENDPOINT ĐÃ SỬA LỖI: Lấy Bài báo Liên quan
//    @GetMapping("/{slug}")
//    public Object getRelatedArticles(@PathVariable String slug) {
//        // Gọi phương thức findRelatedBySlug đã được triển khai trong Service
//        return articleService.findRelatedBySlug(slug);
//    }
//}