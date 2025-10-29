# 🏙️ GoldenLand
**Website quản lý bất động sản** – Dự án nhóm môn Phát triển ứng dụng web Java Spring Boot.

![GoldenLand Banner](https://dummyimage.com/1200x250/222/ffd700&text=GoldenLand+-+Real+Estate+Management)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![MySQL](https://img.shields.io/badge/Database-MySQL-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📑 Mục lục
- [Giới thiệu](#giới-thiệu)
- [Mục tiêu hệ thống](#mục-tiêu-hệ-thống)
- [Chức năng chính](#chức-năng-chính)
- [Kiến trúc & Công nghệ](#kiến-trúc--công-nghệ)
- [Yêu cầu môi trường](#yêu-cầu-môi-trường)
- [Cấu hình](#cấu-hình)
- [Cơ sở dữ liệu & Migration](#cơ-sở-dữ-liệu--migration)
- [Cách chạy ứng dụng](#cách-chạy-ứng-dụng)
- [Docker Compose](#docker-compose)
- [Tài khoản mẫu & Phân quyền](#tài-khoản-mẫu--phân-quyền)
- [API Docs](#api-docs)
- [Cấu trúc thư mục](#cấu-trúc-thư-mục)
- [Test](#test)
- [Troubleshooting / FAQ](#troubleshooting--faq)
- [Đóng góp & License](#đóng-góp--license)
- [Thành viên nhóm](#thành-viên-nhóm)
- [Demo / Hình ảnh](#demo--hình-ảnh)

---

## 🏠 Giới thiệu
**GoldenLand** là ứng dụng quản lý bất động sản giúp người dùng đăng, quản lý và tìm kiếm tin rao mua/bán/cho thuê bất động sản.

Có 2 nhóm người dùng chính:
- **Admin / Staff / Manager:** quản trị dữ liệu (Users, Customers, Buildings).
- **Customer:** đăng ký, đăng nhập, quản lý hồ sơ, đăng bài, thanh toán.

---

## 🎯 Mục tiêu hệ thống
- Xây dựng website full-stack Java Spring Boot có:
  - Phân quyền & xác thực người dùng.
  - Upload ảnh (Cloudinary).
  - Thanh toán trực tuyến (VNPAY sandbox).
  - Chat WebSocket.
  - Quản lý schema bằng Flyway.

---

## ⚙️ Chức năng chính
| Chức năng | Vai trò sử dụng | Mô tả |
|------------|-----------------|-------|
| Đăng nhập / Phân quyền | Tất cả | Phân quyền Admin, Staff, Manager, Customer |
| CRUD Người dùng | Admin | Thêm, sửa, xóa, khóa tài khoản |
| CRUD Bất động sản | Admin/Staff/Customer | Đăng, sửa, xóa, duyệt bài |
| Quản lý hồ sơ khách hàng | Staff/Manager | Xem và cập nhật thông tin |
| Upload ảnh Cloudinary | Customer | Tải ảnh khi đăng bài |
| Quên mật khẩu qua Email | Tất cả | Gửi mail reset password |
| Thanh toán VNPAY | Customer | Thanh toán đăng bài VIP |
| Chat phân công (WebSocket) | Staff/Manager | Trao đổi nội bộ |
| Migration schema (Flyway) | DevOps | Quản lý thay đổi CSDL |

---

## 🧩 Kiến trúc & Công nghệ
- **Ngôn ngữ:** Java 17+
- **Framework:** Spring Boot (Spring MVC, Spring Security, Spring Data JPA, WebSocket)
- **Frontend:** Thymeleaf, Bootstrap, jQuery, AJAX
- **Database:** MySQL + Flyway Migration
- **Upload ảnh:** Cloudinary
- **Thanh toán:** VNPAY sandbox
- **Email:** Spring Mail (SMTP Gmail)
- **Build tool:** Maven
- **Dev tools:** IntelliJ / VS Code, PlantUML, Git
- **Logging:** SLF4J + Logback

---

## 🧱 Yêu cầu môi trường
- Java JDK **17+**
- MySQL Server
- Maven hoặc Maven Wrapper
- (Tùy chọn) Cloudinary account
- (Tùy chọn) Gmail App Password
- (Tùy chọn) VNPAY sandbox keys

---

## ⚙️ Cấu hình
Tạo file `src/main/resources/application.properties`:

```properties
# Server
server.port=8092

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/estateadvance?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=123456

# JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Cloudinary
cloudinary.cloud_name=your_cloud_name
cloudinary.api_key=your_api_key
cloudinary.api_secret=your_api_secret

# VNPAY sandbox
vnpay.tmnCode=VNPAYCODE
vnpay.hashSecret=VNPAYSECRET
vnpay.payUrl=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=http://localhost:8092/web/payment/return
```

---

## 🗄️ Cơ sở dữ liệu & Migration
- **Flyway** quản lý tại: `src/main/resources/db/migration`
- Các file migration:
  - `V1__init_schema.sql`
  - `V2__seed_roles_users.sql`
  - `V3__seed_sample_data.sql`
- **ER Diagram:** `docs/database/ERD.png`

Bảng chính: `user`, `role`, `user_role`, `customer`, `building`, `payment`.

---

## ▶️ Cách chạy ứng dụng
```bash
# Tạo database trống
CREATE DATABASE estateadvance CHARACTER SET utf8mb4;

# Build & chạy
mvn clean package
mvn spring-boot:run

# Mở trình duyệt:
http://localhost:8092
```

---

## 🐳 Docker Compose
```yaml
version: "3.8"
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: estateadvance
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
  app:
    build: .
    depends_on:
      - mysql
    ports:
      - "8092:8092"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/estateadvance
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
```

---

## 👤 Tài khoản mẫu & Phân quyền
| Role | Email | Mật khẩu | Quyền hạn |
|------|--------|-----------|------------|
| Admin | admin@gl.com | Admin@123 | Toàn quyền |
| Manager | manager@gl.com | Manager@123 | Duyệt bài, phân công |
| Staff | staff@gl.com | Staff@123 | Quản lý khách hàng |
| Customer | user@gl.com | User@123 | Đăng bài cá nhân |

---

## 🔗 API Docs
- Swagger UI: http://localhost:8092/swagger-ui/index.html  
- OpenAPI JSON: `/v3/api-docs`  
- Postman Collection: `docs/postman/GoldenLand.postman_collection.json`

---

## 🧱 Cấu trúc thư mục
```
src/
 ├─ main/java/com/example/demo/
 │   ├─ controller/
 │   │   ├─ web/
 │   │   └─ api/
 │   ├─ service/
 │   ├─ repository/
 │   ├─ entity/
 │   ├─ config/
 │   └─ security/
 ├─ resources/
 │   ├─ templates/
 │   ├─ static/
 │   ├─ db/migration/
 │   └─ application.properties
docs/
 ├─ images/
 ├─ database/
 ├─ postman/
 └─ UserManual.docx
```

---

## 🧪 Test
```bash
mvn test
```
Các test bao gồm:  
✅ Unit test cho Service, Repository  
✅ Integration test cho API đăng nhập & thanh toán  

---

## 💡 Troubleshooting / FAQ
- **Lỗi Gmail 535-5.7.8:** cần dùng App Password.  
- **Lỗi MySQL Public Key Retrieval:** thêm `allowPublicKeyRetrieval=true`.  
- **VNPAY checksum sai:** kiểm tra `hashSecret`, encode URL đúng thứ tự.  
- **Cloudinary 401:** sai `api_key` hoặc `api_secret`.  

---

## 🤝 Đóng góp & License
- Quy ước commit: `feat:`, `fix:`, `docs:`, `refactor:`
- Branches: `main` (stable), `dev`, `feature/*`
- License: MIT License

---

## 👥 Thành viên nhóm
- **Phan Duy Khương**
- **Phạm Huỳnh Khánh Linh**
- **Mai Hoàng Trúc Lâm**
- **Lục Nhật Khôi**

---

## 🖼️ Demo / Hình ảnh
Link demo: [http://localhost:8092](http://localhost:8092)  
Thư mục ảnh minh họa: `docs/images/`  
![Trang chủ](docs/images/home.png)
![Quản lý bài đăng](docs/images/building.png)
![Thanh toán VNPAY](docs/images/payment.png)
