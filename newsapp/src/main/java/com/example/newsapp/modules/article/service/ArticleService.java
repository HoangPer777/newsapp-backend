package com.example.newsapp.modules.article.service;

import com.example.newsapp.modules.account.entity.User;
import com.example.newsapp.modules.account.repository.UserRepository;
import com.example.newsapp.modules.article.entity.Article;
import com.example.newsapp.modules.article.repository.ArticleRepository;
import com.example.newsapp.modules.author.entity.Author;
import com.example.newsapp.modules.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired private AuthorRepository authorRepository;

    @Autowired
    private RestTemplate restTemplate; // 2. INJECT RESTTEMPLATE
    private final String PYTHON_SYNC_URL = "http://10.0.2.2:8000/sync";


    public List<Article> getLatestArticles() {
        return articleRepository.findTop20ByOrderByCreatedAtDesc();
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> getMostViewedArticles() {
        return articleRepository.findTop20ByOrderByViewCountDesc();
    }

      public List<Article> getMostLikedArticles() {
        return articleRepository.findTop20ByOrderByLikeCountDesc();
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

    // public Article createArticle(Article article) {
    // // debug incoming article
    // log.info("createArticle called: title='{}' slug='{}'", article == null ? null
    // : article.getTitle(), article == null ? null : article.getSlug());
    //
    // // set created time if missing
    // if (article.getCreatedAt() == null) {
    // article.setCreatedAt(LocalDateTime.now());
    // }

    // // generate or normalize slug (use provided slug if present, otherwise
    // generate from title)
    // String sourceForSlug = (article.getSlug() != null &&
    // !article.getSlug().trim().isEmpty())
    // ? article.getSlug()
    // : (article.getTitle() == null ? "article" : article.getTitle());
    // String base = slugify(sourceForSlug);
    // String candidate = base;
    // int suffix = 0;
    // while (articleRepository.existsBySlug(candidate)) {
    // suffix++;
    // candidate = base + "-" + suffix;
    // }
    // article.setSlug(candidate);
    //
    // return articleRepository.save(article);
    // }
    public Article getArticleBySlug(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Slug not found"));
    }

// CẬP NHẬT HÀM NÀY: Nhận thêm tham số email của người đăng nhập
public Article createArticle(Article article, String email) {
    log.info("createArticle called: title='{}' by user='{}'", article.getTitle(), email);

    // 2. Tìm User từ Email (Token)
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người dùng"));

    // 3. Tìm Author tương ứng với User đó
    Author author = authorRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Tài khoản này chưa được cấp quyền Tác giả"));

    // 4. Gán Author (đúng kiểu dữ liệu Author) vào bài báo
    article.setAuthor(author.getUser());

    // 3. Tự động điền thông tin mặc định nếu thiếu
    if (article.getCreatedAt() == null) {
        article.setCreatedAt(LocalDateTime.now());
    }
    if (article.getViewCount() == 0) {
        article.setViewCount(0);
    }
    if (article.getLikeCount() == 0) {
        article.setLikeCount(0);
    }
        // 3. Xử lý SLUG
        // Nếu không gửi slug lên, lấy title làm slug. Nếu có thì dùng slug đó.
        String sourceForSlug = (article.getSlug() != null && !article.getSlug().trim().isEmpty())
                ? article.getSlug()
                : (article.getTitle() == null ? "article" : article.getTitle());
        // Tạo slug chuẩn (bỏ dấu, khoảng trắng...)
        String baseSlug = slugify(sourceForSlug);
        String finalSlug = baseSlug;
        // 4. Kiểm tra trùng Slug trong database
        // Nếu đã có bài "tin-tuc", nó sẽ đổi thành "tin-tuc-1", "tin-tuc-2"...
        int suffix = 1;
        while (articleRepository.existsBySlug(finalSlug)) {
            finalSlug = baseSlug + "-" + suffix;
            suffix++;
        }
        article.setSlug(finalSlug);
        // 5. Lưu vào DB
//        return articleRepository.save(article);
    Article savedArticle = articleRepository.save(article);
    // 6. GỌI PYTHON ĐỂ ĐỒNG BỘ CHATBOT
    try {
        log.info("Đang gọi Python để sync bài báo mới cho Chatbot...");
        // Gọi endpoint /sync mà Han vừa gửi bên Python
        restTemplate.postForEntity(PYTHON_SYNC_URL, null, String.class);
        log.info("Đồng bộ Chatbot thành công!");
    } catch (Exception e) {
        // Chúng ta dùng try-catch để nếu Python lỗi thì bài báo vẫn được lưu ở Java
        log.error("Lỗi khi đồng bộ Chatbot (Python có thể chưa chạy): {}", e.getMessage());
    }

    return savedArticle;
    }

    private String slugify(String input) {
        if (input == null)
            return "article";
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
        if (slug.isEmpty())
            return "article";
        return slug;
    }

}
