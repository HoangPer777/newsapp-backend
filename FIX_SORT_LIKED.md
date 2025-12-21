# 🔧 Đã sửa lỗi sắp xếp lọc yêu thích

## ❌ Vấn đề
Kết quả API `GET /api/articles?sort=most_liked` không được sắp xếp đúng theo `likeCount` giảm dần.

**Kết quả hiện tại (SAI):**
- id: 8, likeCount: 134
- id: 7, likeCount: 201
- id: 6, likeCount: 67
- ...

**Kết quả mong đợi (ĐÚNG):**
- id: 2, likeCount: 256 ← Cao nhất
- id: 7, likeCount: 201 ← Thứ 2
- id: 5, likeCount: 178 ← Thứ 3
- id: 8, likeCount: 134 ← Thứ 4
- ...

## ✅ Đã sửa
Đã cập nhật method `findTop20ByOrderByLikeCountDesc()` trong `ArticleRepository.java` để sử dụng native SQL query rõ ràng hơn, đảm bảo sắp xếp đúng.

## 🔄 Cách rebuild và test lại

### Bước 1: Rebuild project
```bash
cd newsapp
./mvnw clean compile
```

Hoặc nếu dùng Docker:
```bash
docker-compose down
docker-compose up --build
```

### Bước 2: Test lại trên Postman
1. Đảm bảo server đã restart
2. Gửi request: `GET http://localhost:8080/api/articles?sort=most_liked`
3. Kiểm tra kết quả:
   - Bài viết đầu tiên phải có `likeCount` cao nhất (256)
   - Các bài viết sau sắp xếp giảm dần theo `likeCount`

### Bước 3: Xác nhận kết quả đúng
Kết quả phải là:
```json
[
  {
    "id": 2,
    "likeCount": 256  ← Cao nhất
  },
  {
    "id": 7,
    "likeCount": 201  ← Thứ 2
  },
  {
    "id": 5,
    "likeCount": 178  ← Thứ 3
  },
  {
    "id": 8,
    "likeCount": 134  ← Thứ 4
  },
  {
    "id": 4,
    "likeCount": 123  ← Thứ 5
  },
  {
    "id": 1,
    "likeCount": 89   ← Thứ 6
  },
  {
    "id": 6,
    "likeCount": 67   ← Thứ 7
  },
  {
    "id": 3,
    "likeCount": 45   ← Thứ 8
  }
]
```

## 📝 Thay đổi code

**File:** `newsapp/src/main/java/com/example/newsapp/modules/article/repository/ArticleRepository.java`

**Trước:**
```java
List<Article> findTop20ByOrderByLikeCountDesc();
```

**Sau:**
```java
@Query(value = "SELECT * FROM articles ORDER BY like_count DESC LIMIT 20", nativeQuery = true)
List<Article> findTop20ByOrderByLikeCountDesc();
```

Sử dụng native SQL query để đảm bảo sắp xếp chính xác theo `like_count` giảm dần.

---

**Sau khi rebuild, test lại và cho tôi biết kết quả!** 🚀

