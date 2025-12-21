# Hướng dẫn Test API trên Postman - NewsApp Backend

## 📋 Mục lục
1. [Thiết lập Postman](#thiết-lập-postman)
2. [Test API Lọc Yêu Thích](#test-api-lọc-yêu-thích)
3. [Các API khác](#các-api-khác)
4. [Collection Postman](#collection-postman)

---

## 🚀 Thiết lập Postman

### Bước 1: Khởi động Server
Đảm bảo backend đang chạy:
```bash
# Nếu dùng Docker
docker-compose up

# Hoặc chạy trực tiếp
cd newsapp
./mvnw spring-boot:run
```

Server sẽ chạy tại: **http://localhost:8080**

### Bước 2: Tạo Environment trong Postman (Tùy chọn)
1. Click **Environments** → **+** (Create Environment)
2. Đặt tên: `NewsApp Local`
3. Thêm biến:
   - `base_url`: `http://localhost:8080`
   - `token`: (để trống, sẽ điền sau khi login)
4. Click **Save**

---

## ❤️ Test API Lọc Yêu Thích (Most Liked)

### API Endpoint: Lấy bài viết được yêu thích nhất

**Method:** `GET`  
**URL:** `http://localhost:8080/api/articles?sort=most_liked`  
**Authentication:** Không cần (Public API)

### Cách test:

#### 1. Tạo Request mới
- Click **New** → **HTTP Request**
- Đặt tên: `Get Most Liked Articles`

#### 2. Cấu hình Request
- **Method:** Chọn `GET`
- **URL:** Nhập `http://localhost:8080/api/articles`
- **Params (Query Parameters):**
  - Key: `sort`
  - Value: `most_liked`
  - Click **Add**

#### 3. Gửi Request
- Click nút **Send** (màu xanh)
- Xem kết quả ở tab **Body** (phía dưới)

#### 4. Kết quả mong đợi
```json
[
  {
    "id": 2,
    "title": "Giải bóng đá World Cup 2024: Những khoảnh khắc đáng nhớ",
    "content": "...",
    "summary": "...",
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
    "likeCount": 256  // ← Sắp xếp theo likeCount giảm dần
  },
  {
    "id": 7,
    "title": "Công nghệ: Smartphone mới với camera AI siêu nét",
    "likeCount": 201,  // ← Xếp thứ 2
    ...
  },
  ...
]
```

**Lưu ý:** Kết quả trả về tối đa 20 bài viết, sắp xếp theo `likeCount` giảm dần (nhiều like nhất ở trên).

---

## 📚 Các API khác

### 1. Lấy bài viết mới nhất
**GET** `http://localhost:8080/api/articles?sort=newest`

**Query Params:**
- `sort`: `newest`

**Kết quả:** 20 bài viết mới nhất, sắp xếp theo `createdAt` giảm dần

---

### 2. Lấy bài viết xem nhiều nhất
**GET** `http://localhost:8080/api/articles?sort=most_viewed`

**Query Params:**
- `sort`: `most_viewed`

**Kết quả:** 20 bài viết có lượt xem cao nhất, sắp xếp theo `viewCount` giảm dần

---

### 3. Lọc theo danh mục (Category)
**GET** `http://localhost:8080/api/articles?category=Technology`

**Query Params:**
- `category`: `Technology` (hoặc `Sports`, `Economy`, `Travel`, `Health`, `Education`)

**Ví dụ các category có sẵn:**
- `Technology`
- `Sports`
- `Economy`
- `Travel`
- `Health`
- `Education`

---

### 4. Tìm kiếm bài viết
**GET** `http://localhost:8080/api/articles/search?q=AI`

**Query Params:**
- `q`: Từ khóa tìm kiếm (ví dụ: `AI`, `World Cup`, `Việt Nam`)

**Kết quả:** Các bài viết có title chứa từ khóa (không phân biệt hoa thường)

---

### 5. Lấy chi tiết một bài viết
**GET** `http://localhost:8080/api/articles/{id}`

**Ví dụ:** `http://localhost:8080/api/articles/1`

**Lưu ý:** Mỗi lần gọi API này, `viewCount` sẽ tự động tăng lên 1.

---

### 6. Lấy danh sách mặc định (không có params)
**GET** `http://localhost:8080/api/articles`

**Kết quả:** Trả về bài viết mới nhất (giống `?sort=newest`)

---

## 🔐 API cần Authentication

### Đăng ký tài khoản
**POST** `http://localhost:8080/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "test@example.com",
  "password": "password123",
  "displayName": "Test User"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### Đăng nhập
**POST** `http://localhost:8080/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Lưu ý:** Copy `accessToken` để dùng cho các API cần authentication.

---

### Tạo bài viết mới (Cần JWT)
**POST** `http://localhost:8080/api/articles`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {accessToken}
```

**Body (raw JSON):**
```json
{
  "title": "Bài viết test mới",
  "content": "Nội dung bài viết test...",
  "summary": "Tóm tắt bài viết",
  "category": "Technology",
  "slug": "bai-viet-test-moi",
  "author": {
    "id": 1
  },
  "createdAt": "2024-01-28T10:00:00",
  "updatedAt": "2024-01-28T10:00:00",
  "viewCount": 0,
  "likeCount": 0
}
```

**Lưu ý:** 
- Cần đăng nhập trước để lấy `accessToken`
- `author.id` phải là ID của user đã đăng nhập
- `slug` phải là unique (không trùng với bài viết khác)

---

## 📦 Collection Postman (Import sẵn)

### Tạo Collection mới:
1. Click **Collections** → **+** (New Collection)
2. Đặt tên: `NewsApp API`
3. Tạo các request con:

#### Request 1: Get Most Liked Articles
- Method: `GET`
- URL: `{{base_url}}/api/articles?sort=most_liked`

#### Request 2: Get Newest Articles
- Method: `GET`
- URL: `{{base_url}}/api/articles?sort=newest`

#### Request 3: Get Most Viewed Articles
- Method: `GET`
- URL: `{{base_url}}/api/articles?sort=most_viewed`

#### Request 4: Get Articles by Category
- Method: `GET`
- URL: `{{base_url}}/api/articles?category=Technology`

#### Request 5: Search Articles
- Method: `GET`
- URL: `{{base_url}}/api/articles/search?q=AI`

#### Request 6: Get Article Detail
- Method: `GET`
- URL: `{{base_url}}/api/articles/1`

#### Request 7: Register
- Method: `POST`
- URL: `{{base_url}}/auth/register`
- Body: JSON (như ví dụ trên)

#### Request 8: Login
- Method: `POST`
- URL: `{{base_url}}/auth/login`
- Body: JSON (như ví dụ trên)

#### Request 9: Create Article (Cần Auth)
- Method: `POST`
- URL: `{{base_url}}/api/articles`
- Headers: `Authorization: Bearer {{token}}`
- Body: JSON (như ví dụ trên)

---

## 🧪 Test Cases cho Lọc Yêu Thích

### Test Case 1: Lọc bài viết yêu thích cơ bản
1. Gửi request: `GET /api/articles?sort=most_liked`
2. **Kiểm tra:**
   - Status code: `200 OK`
   - Response là array
   - Các bài viết được sắp xếp theo `likeCount` giảm dần
   - Bài viết đầu tiên có `likeCount` cao nhất

### Test Case 2: So sánh với các sort khác
1. Gửi `GET /api/articles?sort=most_liked`
2. Gửi `GET /api/articles?sort=newest`
3. Gửi `GET /api/articles?sort=most_viewed`
4. **Kiểm tra:** Kết quả khác nhau, sắp xếp đúng theo tiêu chí

### Test Case 3: Kết hợp với category
1. Gửi `GET /api/articles?category=Technology&sort=most_liked`
2. **Lưu ý:** Hiện tại API chỉ hỗ trợ 1 trong 2 (category hoặc sort), không kết hợp được
3. **Kiểm tra:** Nếu có category, sẽ ưu tiên lọc theo category

### Test Case 4: Không có dữ liệu
1. Xóa hết dữ liệu trong bảng articles
2. Gửi `GET /api/articles?sort=most_liked`
3. **Kiểm tra:** Trả về array rỗng `[]`

---

## 🔍 Debug Tips

### 1. Kiểm tra Server đang chạy
- Mở browser: `http://localhost:8080/actuator/health`
- Nếu trả về `{"status":"UP"}` → Server OK

### 2. Kiểm tra Database
- Dùng Navicat hoặc pgAdmin
- Kiểm tra bảng `articles` có dữ liệu không
- Kiểm tra `likeCount` của các bài viết

### 3. Xem Logs
- Nếu dùng Docker: `docker-compose logs -f app`
- Nếu chạy trực tiếp: Xem console output

### 4. Test với Postman Console
- View → Show Postman Console
- Xem request/response chi tiết

---

## 📊 Ví dụ Response đầy đủ

```json
[
  {
    "id": 2,
    "title": "Giải bóng đá World Cup 2024: Những khoảnh khắc đáng nhớ",
    "content": "World Cup 2024 đã kết thúc với nhiều khoảnh khắc đáng nhớ...",
    "summary": "World Cup 2024 đã khép lại với nhiều kỷ lục mới...",
    "category": "Sports",
    "slug": "giai-bong-da-world-cup-2024-nhung-khoanh-khac-dang-nho",
    "author": {
      "id": 3,
      "email": "reporter2@newsapp.com",
      "displayName": "Trần Thị B",
      "passwordHash": "...",
      "createdAt": "2024-01-17T11:15:00"
    },
    "createdAt": "2024-01-21T15:30:00",
    "updatedAt": "2024-01-21T16:00:00",
    "viewCount": 3420,
    "likeCount": 256
  },
  {
    "id": 7,
    "title": "Công nghệ: Smartphone mới với camera AI siêu nét",
    "content": "Hãng công nghệ hàng đầu vừa ra mắt...",
    "summary": "Smartphone mới với camera AI và pin khủng...",
    "category": "Technology",
    "slug": "cong-nghe-smartphone-moi-voi-camera-ai-sieu-net",
    "author": {
      "id": 3,
      "email": "reporter2@newsapp.com",
      "displayName": "Trần Thị B",
      "passwordHash": "...",
      "createdAt": "2024-01-17T11:15:00"
    },
    "createdAt": "2024-01-26T10:00:00",
    "updatedAt": "2024-01-26T10:00:00",
    "viewCount": 2890,
    "likeCount": 201
  }
]
```

---

## ✅ Checklist Test

- [ ] Server đang chạy tại localhost:8080
- [ ] Database có dữ liệu test (đã chạy script SQL)
- [ ] Test API lọc yêu thích: `GET /api/articles?sort=most_liked`
- [ ] Kiểm tra kết quả sắp xếp đúng theo likeCount
- [ ] Test các API sort khác (newest, most_viewed)
- [ ] Test lọc theo category
- [ ] Test tìm kiếm
- [ ] Test đăng ký/đăng nhập
- [ ] Test tạo bài viết (cần auth)

---

## 🆘 Troubleshooting

**Lỗi: "Connection refused"**
→ Server chưa chạy, kiểm tra lại

**Lỗi: "404 Not Found"**
→ Kiểm tra URL có đúng không, đảm bảo có `/api/articles`

**Lỗi: "500 Internal Server Error"**
→ Kiểm tra database connection, xem logs server

**Kết quả trả về rỗng `[]`**
→ Kiểm tra database có dữ liệu không, chạy lại script SQL

**Lỗi: "401 Unauthorized"**
→ API cần JWT token, đăng nhập trước để lấy token

---

**Chúc bạn test thành công! 🎉**


