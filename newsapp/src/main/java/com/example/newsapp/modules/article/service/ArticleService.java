package com.example.newsapp.modules.article.service;

import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public List<Article> search(String keyword, PageRequest pageRequest) {
        return articleRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Article getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article Not Found"));

        if (article.getViewCount() == null) {
            article.setViewCount(0L);
        }
        article.setViewCount(article.getViewCount() + 1);
//        article.setImageUrl(article.getViewCount() + 1);
        return articleRepository.save(article);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

//    // Phương thức mới cho Chi tiết và Liên quan
//    // 1. ⭐ PHƯƠNG THỨC MỚI: Tìm theo Slug (cho frontend dùng URL)
//    public Article findBySlug(String slug) {
//        // Cần đảm bảo ArticleRepository có findBySlug(String slug)
//        return articleRepository.findBySlug(slug)
//                .orElseThrow(() -> new NoSuchElementException("Article Not Found with Slug: " + slug));
//    }
//
//    // 2. ⭐ PHƯƠNG THỨC MỚI: Tìm bài báo chi tiết VÀ tăng View Count (dùng Slug)
//    public Article getArticleDetailBySlug(String slug) {
//        Article article = findBySlug(slug); // Lấy bài báo (có xử lý lỗi)
//
//        // Tăng View Count (Logic tương tự getArticleById)
//        Long currentViews = Optional.ofNullable(article.getViewCount()).orElse(0L);
//        article.setViewCount(currentViews + 1);
//
//        // Lưu và trả về bài báo đã cập nhật
//        return articleRepository.save(article);
//    }
//
//    // 3. ⭐ PHƯƠNG THỨC MỚI: Tìm các bài báo liên quan
//    /**
//     * Tìm 4 bài báo mới nhất, cùng category với bài báo hiện tại, nhưng không phải bài báo đó.
//     */
////    public List<Article> findRelatedArticles(String slug) {
////        // ⭐ BƯỚC 1: Tìm bài báo hiện tại để lấy Category/Tags
////        Optional<Article> currentArticleOpt = articleRepository.findBySlug(slug);
////
////        if (currentArticleOpt.isEmpty()) {
////            // Nếu không tìm thấy bài báo chính, trả về danh sách trống
////            return List.of();
////        }
////
////        Article currentArticle = currentArticleOpt.get();
////
////        // ⭐ BƯỚC 2: Định nghĩa tiêu chí tìm kiếm (Ví dụ: 4 bài)
////        Pageable topFour = PageRequest.of(0, 4);
////
////        // ⭐ BƯỚC 3: Gọi Repository để tìm các bài báo cùng Category,
////        // Loại trừ slug hiện tại và sắp xếp theo ngày tạo giảm dần
////        // Lưu ý: Phương thức này cần được khai báo trong ArticleRepository
////        // (Ví dụ: findTop4ByCategoryAndSlugIsNotOrderByCreatedAtDesc)
////        return articleRepository.findTop4ByCategoryAndSlugIsNotOrderByCreatedAtDesc(
////                currentArticle.getCategory(),
////                slug,
////                topFour
////        );
////    }
//    // 3. ⭐ PHƯƠNG THỨC MỚI: Tìm các bài báo liên quan
//    public List<Article> findRelatedBySlug(String slug) {
//        Optional<Article> currentArticleOpt = articleRepository.findBySlug(slug);
//
//        if (currentArticleOpt.isEmpty()) {
//            return List.of();
//        }
//
//        Article currentArticle = currentArticleOpt.get();
//
//        // ⭐ ĐÃ SỬA LỖI: Khai báo Pageable đúng và không cần ép kiểu
//        Pageable topFour = PageRequest.of(0, 4); // KHÔNG CẦN ÉP KIỂU
//
//        // ⭐ BƯỚC 3: Gọi Repository để tìm các bài báo cùng Category,
//        // (Yêu cầu ArticleRepository có phương thức: findTop4ByCategoryAndSlugIsNotOrderByCreatedAtDesc)
//        return articleRepository.findTop4ByCategoryAndSlugIsNotOrderByCreatedAtDesc(
//                currentArticle.getCategory(),
//                slug,
//                topFour
//        );
//    }

}

//package com.example.newsapp.modules.article.service;
//
//import com.example.newsapp.modules.article.entity.Article;
//import com.example.newsapp.modules.article.repository.ArticleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page; // ⭐ Cần thêm import này nếu findAll/search trả về Page
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable; // Đúng
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//@Service
//public class ArticleService {
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    // ⭐ PHƯƠNG THỨC CHUNG: Lấy tất cả bài báo (Có phân trang)
//    public Page<Article> findAll(Pageable pageable) {
//        // Trả về Page<Article> từ JpaRepository
//        return articleRepository.findAll(pageable);
//    }
//
//    public List<Article> getLatestArticles() {
//        return articleRepository.findTop20ByOrderByCreatedAtDesc();
//    }
//
//    public List<Article> getMostViewedArticles() {
//        return articleRepository.findTop20ByOrderByViewCountDesc();
//    }
//
//    public List<Article> getArticlesByCategory(String category) {
//        return articleRepository.findByCategoryOrderByCreatedAtDesc(category);
//    }
//
//    public List<Article> search(String keyword, PageRequest pageRequest) {
//        return articleRepository.findByTitleContainingIgnoreCase(keyword);
//    }
//
//    // ⭐ ĐÃ SỬA: Hàm lấy theo ID và Tăng View Count (Sửa RuntimeException thành NoSuchElementException)
//    public Article getArticleById(Long id) {
//        Article article = articleRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Article Not Found with ID: " + id));
//
//        // Tăng View Count an toàn
//        Long currentViews = Optional.ofNullable(article.getViewCount()).orElse(0L);
//        article.setViewCount(currentViews + 1);
//
//        return articleRepository.save(article);
//    }
//
//    public Article createArticle(Article article) {
//        return articleRepository.save(article);
//    }
//
//    // --- PHƯƠNG THỨC CHI TIẾT VÀ LIÊN QUAN ---
//
//    // 1. Tìm theo Slug (cho mục đích nội bộ, không tăng view count)
//    public Article findBySlug(String slug) {
//        return articleRepository.findBySlug(slug)
//                .orElseThrow(() -> new NoSuchElementException("Article Not Found with Slug: " + slug));
//    }
//
//    // 2. Tìm bài báo chi tiết VÀ tăng View Count (dùng Slug)
//    public Article getArticleDetailBySlug(String slug) {
//        Article article = findBySlug(slug);
//
//        // Tăng View Count
//        Long currentViews = Optional.ofNullable(article.getViewCount()).orElse(0L);
//        article.setViewCount(currentViews + 1);
//
//        return articleRepository.save(article);
//    }
//
//    // 3. ⭐ PHƯƠNG THỨC LIÊN QUAN (Tên phương thức bạn muốn sử dụng)
//    public List<Article> findRelatedBySlug(String slug) {
//        Optional<Article> currentArticleOpt = articleRepository.findBySlug(slug);
//
//        if (currentArticleOpt.isEmpty()) {
//            return List.of();
//        }
//
//        Article currentArticle = currentArticleOpt.get();
//
//        Pageable topFour = PageRequest.of(0, 4);
//
//        // Phương thức này yêu cầu ArticleRepository phải được khai báo
//        return articleRepository.findTop4ByCategoryAndSlugIsNotOrderByCreatedAtDesc(
//                currentArticle.getCategory(),
//                slug,
//                topFour
//        );
//    }
//
//    // ⭐ Phương thức findAll cũ đã được thay thế bằng Page<Article> findAll(Pageable pageable)
//    // Tôi đã loại bỏ phương thức findAll() cũ bạn định nghĩa lại ở cuối file để tránh trùng lặp.
//}