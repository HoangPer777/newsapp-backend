# 🧪 Hướng dẫn Test Lọc Yêu Thích trên Postman - Từng Bước

## ⚡ Các bước thực hiện

### Bước 1: Kiểm tra Server đang chạy
Đảm bảo backend đang chạy tại `http://localhost:8080`

**Cách kiểm tra:**
- Mở browser, truy cập: `http://localhost:8080/actuator/health`
- Nếu thấy `{"status":"UP"}` → Server OK ✅
- Nếu không thấy → Chạy server:
  ```bash
  docker-compose up
  # hoặc
  cd newsapp && ./mvnw spring-boot:run
  ```

---

### Bước 2: Mở Postman và tạo Request mới

1. **Mở Postman** (nếu chưa có, tải tại: https://www.postman.com/downloads/)

2. **Tạo Request mới:**
   - Click nút **"New"** (góc trên trái)
   - Chọn **"HTTP Request"**
   - Hoặc nhấn `Ctrl + N` (Windows) / `Cmd + N` (Mac)

3. **Đặt tên request:**
   - Ở ô "Request name", nhập: `Get Most Liked Articles`

---

### Bước 3: Cấu hình Request

#### 3.1. Chọn Method
- Ở dropdown bên trái URL, chọn **GET** (mặc định là GET)

#### 3.2. Nhập URL
- Trong ô URL, nhập:
  ```
  http://localhost:8080/api/articles
  ```

#### 3.3. Thêm Query Parameter
- Click vào tab **"Params"** (bên dưới URL)
- Trong bảng Parameters:
  - **Key**: Nhập `sort`
  - **Value**: Nhập `most_liked`
  - ✅ **Checkbox**: Đảm bảo đã được tích (tự động tích khi nhập)

**Kết quả:** URL sẽ tự động chuyển thành:
```
http://localhost:8080/api/articles?sort=most_liked
```

---

### Bước 4: Gửi Request

1. **Click nút "Send"** (màu xanh, góc phải)
   - Hoặc nhấn `Ctrl + Enter` (Windows) / `Cmd + Enter` (Mac)

2. **Đợi response** (thường mất 1-2 giây)

---

### Bước 5: Xem kết quả

Sau khi click Send, bạn sẽ thấy:

#### Tab "Body" (mặc định)
- **Pretty**: Hiển thị JSON đẹp, dễ đọc
- **Raw**: Hiển thị JSON thô
- **Preview**: Xem dạng HTML (nếu có)

#### Kết quả mong đợi:
```json
[
  {
    "id": 2,
    "title": "Giải bóng đá World Cup 2024: Những khoảnh khắc đáng nhớ",
    "content": "World Cup 2024 đã kết thúc...",
    "summary": "World Cup 2024 đã khép lại...",
    "category": "Sports",
    "slug": "giai-bong-da-world-cup-2024-nhung-khoanh-khac-dang-nho",
    "author": {
      "id": 3,
      "email": "reporter2@newsapp.com",
      "displayName": "Trần Thị B"
    },
    "createdAt": "2024-01-21T15:30:00",
    "updatedAt": "2024-01-21T16:00:00",
    "viewCount": 3420,
    "likeCount": 256    ← Bài viết này có nhiều like nhất
  },
  {
    "id": 7,
    "title": "Công nghệ: Smartphone mới với camera AI siêu nét",
    "likeCount": 201,    ← Xếp thứ 2
    ...
  },
  {
    "id": 8,
    "title": "Thể thao: Vận động viên Việt Nam giành huy chương vàng SEA Games",
    "likeCount": 134,    ← Xếp thứ 3
    ...
  }
]
```

#### Tab "Headers"
- Xem các header của response (Content-Type, Status, v.v.)

#### Tab "Status"
- **200 OK**: Thành công ✅
- **404 Not Found**: URL sai hoặc server chưa chạy ❌
- **500 Internal Server Error**: Lỗi server hoặc database ❌

---

## ✅ Kiểm tra kết quả đúng

### 1. Kiểm tra Status Code
- Phải là **200 OK** (màu xanh)

### 2. Kiểm tra Response Body
- Phải là một **array** `[...]`
- Mỗi phần tử là một **object** bài viết
- Các bài viết được sắp xếp theo **`likeCount` giảm dần**
  - Bài viết đầu tiên có `likeCount` cao nhất
  - Bài viết cuối cùng có `likeCount` thấp nhất

### 3. Kiểm tra dữ liệu
- Mỗi bài viết có đầy đủ: `id`, `title`, `content`, `summary`, `category`, `slug`, `author`, `createdAt`, `updatedAt`, `viewCount`, `likeCount`
- `author` là một object chứa thông tin user

---

## 🔍 So sánh với các sort khác

Để kiểm tra API lọc yêu thích hoạt động đúng, bạn có thể so sánh với các sort khác:

### Test 1: Most Liked (Yêu thích nhất)
```
GET http://localhost:8080/api/articles?sort=most_liked
```
→ Sắp xếp theo `likeCount` giảm dần

### Test 2: Newest (Mới nhất)
```
GET http://localhost:8080/api/articles?sort=newest
```
→ Sắp xếp theo `createdAt` giảm dần

### Test 3: Most Viewed (Xem nhiều nhất)
```
GET http://localhost:8080/api/articles?sort=most_viewed
```
→ Sắp xếp theo `viewCount` giảm dần

**Kết quả:** 3 request này sẽ trả về thứ tự khác nhau, chứng tỏ API hoạt động đúng!

---

## 🐛 Troubleshooting

### Lỗi: "Could not get response"
**Nguyên nhân:** Server chưa chạy
**Giải pháp:**
1. Kiểm tra server: `http://localhost:8080/actuator/health`
2. Nếu không thấy → Chạy server:
   ```bash
   docker-compose up
   ```

### Lỗi: "404 Not Found"
**Nguyên nhân:** URL sai
**Giải pháp:**
- Kiểm tra URL: `http://localhost:8080/api/articles?sort=most_liked`
- Đảm bảo có `?sort=most_liked` ở cuối

### Lỗi: "500 Internal Server Error"
**Nguyên nhân:** Database chưa có dữ liệu hoặc lỗi kết nối
**Giải pháp:**
1. Kiểm tra database đã có dữ liệu chưa (dùng Navicat)
2. Nếu chưa có → Chạy script SQL trong file `init/create_tables_and_test_data.sql`

### Response trả về `[]` (mảng rỗng)
**Nguyên nhân:** Database chưa có dữ liệu
**Giải pháp:**
1. Mở Navicat
2. Chạy file `init/create_tables_and_test_data.sql`
3. Kiểm tra bảng `articles` có dữ liệu không

### Response không sắp xếp đúng
**Nguyên nhân:** Có thể dữ liệu test chưa có `likeCount` khác nhau
**Giải pháp:**
- Kiểm tra trong database, đảm bảo các bài viết có `likeCount` khác nhau
- Hoặc cập nhật `likeCount` thủ công trong Navicat để test

---

## 📸 Mô tả giao diện Postman

```
┌─────────────────────────────────────────────────────────┐
│  Postman                                    [☰] [👤]    │
├─────────────────────────────────────────────────────────┤
│  GET  │  http://localhost:8080/api/articles?sort=most_liked  │  [Send] │
├───────┴──────────────────────────────────────────────────┤
│  Params │ Authorization │ Headers │ Body │ Pre-request │ Tests │
├───────────────────────────────────────────────────────────┤
│  Query Params                                             │
│  ┌──────────┬──────────────┬─────┐                       │
│  │ KEY      │ VALUE        │ ✓   │                       │
│  ├──────────┼──────────────┼─────┤                       │
│  │ sort     │ most_liked   │ ✓   │                       │
│  └──────────┴──────────────┴─────┘                       │
├───────────────────────────────────────────────────────────┤
│  Body │ Cookies │ Headers │ Test Results │                │
│  ┌────────────────────────────────────────────────────┐   │
│  │ Status: 200 OK                                    │   │
│  │ Time: 123 ms                                      │   │
│  │ Size: 2.5 KB                                      │   │
│  ├────────────────────────────────────────────────────┤   │
│  │ [                                                  │   │
│  │   {                                                │   │
│  │     "id": 2,                                      │   │
│  │     "title": "Giải bóng đá...",                   │   │
│  │     "likeCount": 256                              │   │
│  │   },                                               │   │
│  │   ...                                              │   │
│  │ ]                                                  │   │
│  └────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────┘
```

---

## 🎯 Checklist Test

Trước khi test, đảm bảo:
- [ ] Server đang chạy tại `http://localhost:8080`
- [ ] Database đã có dữ liệu (đã chạy script SQL)
- [ ] Postman đã được cài đặt và mở

Khi test:
- [ ] Method là `GET`
- [ ] URL đúng: `http://localhost:8080/api/articles?sort=most_liked`
- [ ] Status code là `200 OK`
- [ ] Response là array các bài viết
- [ ] Bài viết được sắp xếp theo `likeCount` giảm dần

---

## 💡 Tips

1. **Lưu Request để dùng lại:**
   - Click **Save** sau khi test thành công
   - Tạo Collection để quản lý nhiều request

2. **Import Collection có sẵn:**
   - File → Import → Chọn `NewsApp_API.postman_collection.json`
   - Có sẵn tất cả các API, chỉ cần click Send

3. **Xem Console để debug:**
   - View → Show Postman Console
   - Xem chi tiết request/response

4. **Test nhanh với Environment:**
   - Tạo Environment với biến `base_url = http://localhost:8080`
   - Dùng `{{base_url}}/api/articles?sort=most_liked`

---

**Chúc bạn test thành công! 🎉**

Nếu gặp vấn đề, kiểm tra:
1. Server có đang chạy không?
2. Database có dữ liệu không?
3. URL có đúng không?

