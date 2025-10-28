# News App Backend

Ứng dụng backend cho News App được xây dựng với Spring Boot, PostgreSQL và Docker.

## 🚀 Yêu cầu hệ thống

- **Java 17+** (hoặc Java 21)
- **Docker Desktop** (Windows/Mac/Linux)
- **Maven 3.6+** (tùy chọn, nếu muốn build trực tiếp)

## 📦 Cài đặt và chạy

### Cách 1: Sử dụng Docker (Khuyến nghị)

1. **Clone project**:
   ```bash
   git clone <repository-url>
   cd newsapp-backend
   ```

2. **Khởi động Docker Desktop**

3. **Chạy tất cả services**:
   ```bash
   docker compose up -d
   ```

4. **Kiểm tra services**:
   ```bash
   docker compose ps
   ```

### Cách 2: Chạy thủ công

1. **Khởi động database**:
   ```bash
   docker compose up db -d
   ```

2. **Build và chạy Spring Boot**:
   ```bash
   cd newsapp
   mvn clean package -DskipTests
   java -jar target/newsapp-0.0.1-SNAPSHOT.jar
   ```

## 🌐 Truy cập ứng dụng

- **Spring Boot API**: http://localhost:8080
- **pgAdmin** (Database Management): http://localhost:5050
  - Email: `admin@example.com`
  - Password: `admin`
- **PostgreSQL**: localhost:5432

## 🗄️ Cấu hình Database

### Kết nối từ pgAdmin:
1. Mở http://localhost:5050
2. Đăng nhập với thông tin trên
3. Thêm server mới:
   - **Host**: `newsapp-pg`
   - **Port**: `5432`
   - **Database**: `newsapp`
   - **Username**: `postgres`
   - **Password**: `postgres`

### Kết nối từ ứng dụng khác:
```
Host: localhost
Port: 5432
Database: newsapp
Username: postgres
Password: postgres
```

## 🔧 Cấu hình

### Environment Variables (.env)
Tạo file `.env` trong thư mục gốc:
```env
# Database
POSTGRES_DB=newsapp
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Spring Boot
SPRING_PROFILES_ACTIVE=postgres
SERVER_PORT=8080
```

### Spring Boot Profiles
- `application.yml`: Cấu hình mặc định
- `application-postgres.yml`: Cấu hình cho PostgreSQL

## 📁 Cấu trúc project

```
newsapp-backend/
├── newsapp/                    # Spring Boot application
│   ├── src/main/java/         # Java source code
│   ├── src/main/resources/    # Configuration files
│   ├── pom.xml               # Maven dependencies
│   └── target/               # Build output
├── docker-compose.yml        # Docker services
├── Dockerfile               # Spring Boot container
├── .env                     # Environment variables
└── README.md               # This file
```

## 🛠️ Development

### Build project:
```bash
cd newsapp
mvn clean package
```

### Run tests:
```bash
cd newsapp
mvn test
```

### View logs:
```bash
# All services
docker compose logs

# Specific service
docker compose logs newsapp-app
docker compose logs newsapp-pg
```

## 🐛 Troubleshooting

### Docker không chạy được:
1. Đảm bảo Docker Desktop đã khởi động
2. Kiểm tra: `docker info`

### Port bị chiếm:
1. Kiểm tra port đang sử dụng: `netstat -an | findstr :8080`
2. Thay đổi port trong `.env` file

### Database connection failed:
1. Kiểm tra PostgreSQL container: `docker compose ps`
2. Xem logs: `docker compose logs newsapp-pg`

## 📝 API Endpoints

- `GET /api/articles` - Lấy danh sách bài viết
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký

## 🤝 Contributing

1. Fork project
2. Tạo feature branch
3. Commit changes
4. Push to branch
5. Tạo Pull Request

## 📄 License

MIT License

