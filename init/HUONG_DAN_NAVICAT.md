# Hướng dẫn tạo bảng trong Navicat

## Cách 1: Chạy file SQL (Khuyến nghị - Nhanh nhất)

### Bước 1: Kết nối Database
1. Mở Navicat
2. Tạo kết nối mới đến PostgreSQL (nếu chưa có):
   - Click **Connection** → **PostgreSQL**
   - Điền thông tin:
     - **Connection Name**: NewsApp DB (tên tùy ý)
     - **Host**: `localhost` hoặc `127.0.0.1`
     - **Port**: `5432`
     - **Database**: `newsapp`
     - **Username**: `postgres`
     - **Password**: `postgres`
   - Click **Test Connection** để kiểm tra
   - Click **OK** để lưu

### Bước 2: Mở Query Editor
1. Click chuột phải vào connection **NewsApp DB**
2. Chọn **Open Database** (nếu chưa mở)
3. Click chuột phải vào database `newsapp`
4. Chọn **New Query** hoặc nhấn `Ctrl+Q`

### Bước 3: Mở file SQL
1. Trong cửa sổ Query Editor, click **File** → **Open File**
2. Chọn file `create_tables_and_test_data.sql` trong thư mục `init`
3. File SQL sẽ hiển thị trong editor

### Bước 4: Chạy Script
1. Click nút **Run** (▶) trên thanh công cụ
   - Hoặc nhấn phím `F5`
   - Hoặc click **Query** → **Execute**
2. Đợi script chạy xong (sẽ mất vài giây)
3. Kiểm tra kết quả ở tab **Messages** phía dưới

### Bước 5: Kiểm tra kết quả
1. Trong cây bên trái, refresh database `newsapp` (click chuột phải → **Refresh**)
2. Mở rộng **Tables** để xem 2 bảng: `users` và `articles`
3. Click chuột phải vào bảng → **Open Table** để xem dữ liệu

---

## Cách 2: Tạo bảng thủ công bằng giao diện

### Tạo bảng `users`:

1. Click chuột phải vào **Tables** → **New Table**
2. Đặt tên bảng: `users`
3. Thêm các cột:

   | Column Name | Data Type | Length | Not Null | Primary Key | Auto Increment | Default |
   |-------------|-----------|--------|----------|-------------|----------------|---------|
   | id | bigint | - | ✓ | ✓ | ✓ | - |
   | email | varchar | 255 | ✓ | - | - | - |
   | password_hash | varchar | 255 | ✓ | - | - | - |
   | display_name | varchar | 255 | - | - | - | - |
   | created_at | timestamp | - | ✓ | - | - | CURRENT_TIMESTAMP |

4. Thiết lập Primary Key:
   - Chọn cột `id` → Check **Primary Key**
   - Chọn **Auto Increment**

5. Thiết lập Unique cho email:
   - Chọn cột `email` → Tab **Indexes** → Click **+**
   - Type: **UNIQUE**
   - Columns: `email`

6. Click **Save** (Ctrl+S)

### Tạo bảng `articles`:

1. Click chuột phải vào **Tables** → **New Table**
2. Đặt tên bảng: `articles`
3. Thêm các cột:

   | Column Name | Data Type | Length | Not Null | Primary Key | Auto Increment | Default |
   |-------------|-----------|--------|----------|-------------|----------------|---------|
   | id | bigint | - | ✓ | ✓ | ✓ | - |
   | title | varchar | 500 | ✓ | - | - | - |
   | content | text | - | ✓ | - | - | - |
   | summary | varchar | 1000 | - | - | - | - |
   | category | varchar | 100 | - | - | - | - |
   | slug | varchar | 255 | ✓ | - | - | - |
   | author_id | bigint | - | ✓ | - | - | - |
   | created_at | timestamp | - | - | - | - | - |
   | updated_at | timestamp | - | - | - | - | - |
   | view_count | integer | - | - | - | - | 0 |
   | like_count | integer | - | - | - | - | 0 |

4. Thiết lập Primary Key cho `id` (giống như bảng users)

5. Thiết lập Unique cho `slug`:
   - Chọn cột `slug` → Tab **Indexes** → Click **+**
   - Type: **UNIQUE**
   - Columns: `slug`

6. Tạo Foreign Key:
   - Tab **Foreign Keys** → Click **+**
   - Foreign Key Name: `fk_articles_author`
   - Referenced Schema: `public`
   - Referenced Table: `users`
   - Foreign Key Column: `author_id`
   - Referenced Column: `id`
   - On Delete: **CASCADE**

7. Click **Save** (Ctrl+S)

### Chèn dữ liệu test:

Sau khi tạo xong 2 bảng, bạn có thể:
- Mở file SQL `create_tables_and_test_data.sql`
- Copy phần INSERT (từ dòng "-- Chèn dữ liệu test" trở đi)
- Paste vào Query Editor và chạy (F5)

---

## Cách 3: Import từ file SQL (Nếu có lỗi encoding)

1. Click chuột phải vào database `newsapp`
2. Chọn **Execute SQL File...**
3. Chọn file `create_tables_and_test_data.sql`
4. Click **Start** để chạy
5. Xem kết quả trong cửa sổ log

---

## Kiểm tra dữ liệu đã tạo

### Xem dữ liệu trong bảng:
1. Mở rộng **Tables**
2. Click chuột phải vào bảng `users` → **Open Table**
3. Tương tự với bảng `articles`

### Chạy query kiểm tra:
Mở Query Editor và chạy các câu lệnh sau:

```sql
-- Đếm số lượng users
SELECT COUNT(*) as total_users FROM users;

-- Đếm số lượng articles
SELECT COUNT(*) as total_articles FROM articles;

-- Xem danh sách users
SELECT id, email, display_name, created_at FROM users;

-- Xem danh sách articles với tên tác giả
SELECT 
    a.id, 
    a.title, 
    a.category, 
    a.view_count, 
    a.like_count,
    u.display_name as author
FROM articles a
JOIN users u ON a.author_id = u.id
ORDER BY a.created_at DESC;
```

---

## Lưu ý quan trọng

⚠️ **Nếu bảng đã tồn tại:**
- File SQL có lệnh `DROP TABLE` ở đầu để xóa bảng cũ
- Nếu muốn giữ dữ liệu cũ, hãy comment (--) 2 dòng DROP TABLE đầu tiên

⚠️ **Nếu gặp lỗi encoding:**
- Đảm bảo file SQL được lưu với encoding UTF-8
- Trong Navicat: Tools → Options → General → Character Encoding: UTF-8

⚠️ **Nếu không kết nối được:**
- Kiểm tra PostgreSQL đã chạy chưa (docker-compose up hoặc service đang chạy)
- Kiểm tra port 5432 có bị chiếm không
- Kiểm tra username/password có đúng không

---

## Troubleshooting

**Lỗi: "relation does not exist"**
→ Chạy lại script từ đầu, đảm bảo bảng `users` được tạo trước `articles`

**Lỗi: "duplicate key value violates unique constraint"**
→ Bảng đã có dữ liệu, cần xóa dữ liệu cũ hoặc dùng DROP TABLE

**Lỗi: "permission denied"**
→ Kiểm tra quyền của user `postgres`, đảm bảo có quyền CREATE TABLE


