# News App Backend

Backend cho News App, xây dựng bằng Spring Boot 3, PostgreSQL, Docker Compose.

## Yêu cầu hệ thống
- Java 21 (runtime trong container đã dùng JDK 21)
- Docker Desktop (Windows/Mac/Linux)
- Maven (tùy chọn nếu muốn build JAR ngoài Docker)

## Chạy nhanh với Docker (khuyến nghị)
```bash
# Khởi động toàn bộ stack
docker compose up -d

# Kiểm tra trạng thái
docker compose ps

# Theo dõi log ứng dụng
docker logs -f newsapp-app

# Kiểm tra health
curl -i http://localhost:8080/actuator/health
```

### Dịch vụ trong docker-compose
- `app` (newsapp-app): Spring Boot API (cổng 8080)
- `db` (newsapp-pg): PostgreSQL 16 (cổng 5432)
- `pgadmin` (newsapp-pgadmin): pgAdmin 4 (cổng 5050)

Thông tin truy cập mặc định:
- API: `http://localhost:8080`
- pgAdmin: `http://localhost:5050` (email `admin@example.com` / password `admin`)
- PostgreSQL: host `localhost`, port `5432`, db `newsapp`, user `postgres`, pass `postgres`

## Cấu hình ứng dụng
- Profile mặc định: `postgres` (được bật qua biến `SPRING_PROFILES_ACTIVE`)
- Datasource lấy từ biến môi trường (đã đặt trong `docker-compose.yml`):
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://newsapp-pg:5432/newsapp`
  - `SPRING_DATASOURCE_USERNAME=postgres`
  - `SPRING_DATASOURCE_PASSWORD=postgres`
- Actuator: mở `health, info` và bật probes. Health URL: `/actuator/health`
- Security: cho phép truy cập không cần auth vào `/actuator/**`, `/auth/**`, GET `/articles/**`...

## Build & chạy thủ công (tùy chọn)
```bash
# 1) Chạy Postgres bằng Docker
docker compose up -d db

# 2) Build JAR
cd newsapp
./mvnw clean package -DskipTests

# 3) Chạy JAR (yêu cầu Java 21)
java -jar target/newsapp-0.0.1-SNAPSHOT.jar
```

## Cấu trúc thư mục
```
newsapp-backend/
├── docker-compose.yml
├── Dockerfile
├── config.env
├── newsapp/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/example/newsapp/
│       │   ├── modules/
│       │   │   ├── account/
│       │   │   └── article/
│       │   └── security/
│       └── main/resources/
│           ├── application.yml
│           └── application-postgres.yml
└── README.md
```

## API nhanh
Auth:
```bash
# Đăng ký
curl -i -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user1@example.com","password":"P@ssw0rd!","displayName":"User One"}'

# Đăng nhập
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user1@example.com","password":"P@ssw0rd!"}'
```

Articles (public GET):
```bash
curl -i http://localhost:8080/articles
```

Actuator:
```bash
curl -i http://localhost:8080/actuator/health
```

## Troubleshooting
- Lỗi class file 65 với JDK 17: container đã dùng JDK 21 trong `Dockerfile`.
- Healthcheck `unhealthy`: đảm bảo Actuator bật và container có `curl` (đã cài trong image). Chờ start-period ~45s.
- Kết nối DB lỗi: kiểm tra `newsapp-pg` healthy; xác nhận các biến datasource đúng như ở docker-compose.

## License
MIT

