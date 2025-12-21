# 🧪 Hướng dẫn Test Lọc Yêu Thích trong Navicat

## 📋 Mục đích
Script này giúp bạn kiểm tra và test chức năng lọc yêu thích trực tiếp trong Navicat, so sánh với kết quả từ API.

---

## 🚀 Cách sử dụng

### Bước 1: Mở Navicat
1. Mở Navicat và kết nối đến database `newsapp`
2. Đảm bảo đã kết nối thành công

### Bước 2: Mở Query Editor
1. Click chuột phải vào database `newsapp`
2. Chọn **New Query** (hoặc nhấn `Ctrl+Q`)

### Bước 3: Mở file SQL
1. File → Open File
2. Chọn file: `init/test_filter_most_liked.sql`

### Bước 4: Chạy các query test

#### Query 1: Kiểm tra dữ liệu hiện tại
```sql
SELECT id, title, like_count
FROM articles
ORDER BY id;
```
→ Xem tất cả bài viết và likeCount của chúng

#### Query 2: Test lọc yêu thích (QUAN TRỌNG)
```sql
SELECT 
    a.id,
    a.title,
    a.like_count,
    u.display_name as author
FROM articles a
JOIN users u ON a.author_id = u.id
ORDER BY a.like_count DESC
LIMIT 20;
```
→ **Đây là query giống với API**, kết quả phải sắp xếp theo `like_count` giảm dần

#### Query 3: Kiểm tra thứ tự
```sql
SELECT id, title, like_count
FROM articles
ORDER BY like_count DESC;
```
→ Xem thứ tự sắp xếp có đúng không

---

## ✅ Kết quả mong đợi

Khi chạy query lọc yêu thích, kết quả phải là:

| id | title | like_count |
|----|-------|------------|
| 2 | Giải bóng đá World Cup 2024... | **256** ← Cao nhất |
| 7 | Công nghệ: Smartphone mới... | **201** ← Thứ 2 |
| 5 | Sức khỏe: 5 thói quen... | **178** ← Thứ 3 |
| 8 | Thể thao: Vận động viên... | **134** ← Thứ 4 |
| 4 | Xu hướng du lịch... | **123** ← Thứ 5 |
| 1 | Công nghệ AI... | **89** ← Thứ 6 |
| 6 | Giáo dục: Phương pháp... | **67** ← Thứ 7 |
| 3 | Kinh tế Việt Nam... | **45** ← Thứ 8 |

---

## 🔍 So sánh với API

### Trong Navicat:
Chạy query:
```sql
SELECT id, title, like_count
FROM articles
ORDER BY like_count DESC;
```

### Trong Postman:
Gửi request:
```
GET http://localhost:8080/api/articles?sort=most_liked
```

### Kết quả phải giống nhau!
- Nếu **giống nhau** → API hoạt động đúng ✅
- Nếu **khác nhau** → Có vấn đề với JPA mapping hoặc code

---

## 🐛 Troubleshooting

### Vấn đề 1: Kết quả trong Navicat đúng nhưng API sai
**Nguyên nhân:** Có thể do JPA không map đúng field `likeCount` với `like_count`

**Giải pháp:** 
- Kiểm tra entity `Article.java` có annotation `@Column(name="like_count")` không
- Hoặc đổi tên field trong entity thành `like_count`

### Vấn đề 2: Cả Navicat và API đều sai thứ tự
**Nguyên nhân:** Dữ liệu trong database không đúng

**Giải pháp:**
- Chạy lại script `create_tables_and_test_data.sql`
- Hoặc cập nhật thủ công `like_count` trong Navicat

### Vấn đề 3: Không có dữ liệu
**Nguyên nhân:** Chưa chạy script tạo dữ liệu

**Giải pháp:**
1. Mở file `init/create_tables_and_test_data.sql`
2. Chạy toàn bộ script trong Navicat

---

## 📊 Các query hữu ích khác

### Xem thống kê:
```sql
SELECT 
    MIN(like_count) as min_likes,
    MAX(like_count) as max_likes,
    AVG(like_count) as avg_likes,
    COUNT(*) as total
FROM articles;
```

### Cập nhật likeCount để test:
```sql
-- Tăng likeCount của bài viết id=2
UPDATE articles SET like_count = 300 WHERE id = 2;

-- Sau đó test lại query
SELECT id, title, like_count
FROM articles
ORDER BY like_count DESC;
```

### Xem execution plan (tối ưu query):
```sql
EXPLAIN ANALYZE
SELECT id, title, like_count
FROM articles
ORDER BY like_count DESC
LIMIT 20;
```

---

## 💡 Tips

1. **Lưu query thường dùng:**
   - Sau khi test, lưu query vào Navicat để dùng lại

2. **Tạo View:**
   - Có thể tạo View trong Navicat để query nhanh hơn:
   ```sql
   CREATE VIEW v_most_liked_articles AS
   SELECT id, title, like_count
   FROM articles
   ORDER BY like_count DESC;
   ```

3. **Export kết quả:**
   - Click chuột phải vào kết quả → Export để so sánh với API

---

**Chúc bạn test thành công!** 🎉

Nếu có vấn đề, so sánh kết quả giữa Navicat và Postman để tìm nguyên nhân.


