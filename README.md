# GoldenLand
Website quản lý bất động sản

## Giới thiệu
Ứng dụng quản lý đăng tin bất động sản với 2 nhóm người dùng:
- Admin / Staff / Manager: quản trị dữ liệu (Users, Customers, Buildings).
- Customer: đăng ký, đăng nhập, quản lý hồ sơ, đăng bài (web).

Mục tiêu: minh hoạ một hệ thống web Java full-stack sử dụng Spring Boot, phân quyền, upload ảnh, tích hợp VNPAY và email.

## Tính năng chính
- Đăng nhập/Phân quyền (Admin/Staff/Manager/Customer)
- Quản lý người dùng (CRUD)
- Quản lý khách hàng & hồ sơ
- Đăng / sửa / xoá bài đăng bất động sản (upload ảnh Cloudinary)
- Đổi mật khẩu, quên mật khẩu (email)
- Tích hợp thanh toán VNPAY (sandbox)
- Chat phân công (WebSocket) — cấu hình cơ bản
- Flyway migration để quản lý schema

## Công nghệ sử dụng
- Ngôn ngữ: Java (>=17)
- Framework: Spring Boot (Spring MVC, Spring Security)
- Authentication: CustomUserDetailsService, JWT (nếu có), BCrypt PasswordEncoder
- Template engine: Thymeleaf
- Persistence: Spring Data JPA, Hibernate
- Database: MySQL
- Database migration: Flyway
- Build: Maven (mvn / mvnw)
- Frontend: Bootstrap, jQuery, AJAX, Thymeleaf
- Upload ảnh: Cloudinary
- Thanh toán: VNPAY (sandbox)
- Email: Spring Mail (SMTP Gmail)
- WebSocket / Messaging: Spring WebSocket / STOMP (chat)
- Utils: Lombok, commons-codec (HMAC), SLF4J / Logback
- Dev tools: IDE (VS Code / IntelliJ), Git, PlantUML (diagrams)

## Yêu cầu trước khi cài
- Java 17+
- MySQL server
- Maven
- (Tùy chọn) Cloudinary account để upload ảnh
- (Tùy chọn) VNPAY sandbox keys (đã có trong application.properties mẫu)

## Cấu hình nhanh (application.properties)
- Đặt thông tin kết nối DB: spring.datasource.url/username/password
- Cấu hình Cloudinary: cloudinary.cloud_name, cloudinary.api_key, cloudinary.api_secret
- Cấu hình VNPAY: vnpay.tmnCode, vnpay.hashSecret, vnpay.payUrl, vnpay.returnUrl
- Cấu hình mail SMTP nếu dùng quên mật khẩu

## Chạy ứng dụng (Windows)
1. Import project vào IDE hoặc mở terminal tại thư mục project.
2. Thiết lập database `estateadvance` hoặc thay URL trong application.properties.
3. (Nếu cần) chỉnh file `src/main/resources/application.properties`.
4. Build & run:
   - Sử dụng Maven wrapper:
     - mvnw.cmd clean package
     - mvnw.cmd spring-boot:run
   - Hoặc Maven:
     - mvn clean package
     - mvn spring-boot:run
5. Mở trình duyệt: http://localhost:8092

src/
├─ main/
│  ├─ java/
│  │  └─ com/example/demo/
│  │     ├─ controller/
│  │     │  ├─ web/                # controllers cho giao diện Thymeleaf (/web, /customer, /admin)
│  │     │  └─ api/                # REST API endpoints (/api/**)
│  │     ├─ service/
│  │     │  ├─ impl/               # implement của service
│  │     │  └─ dto/                # nếu có các DTO service-level
│  │     ├─ repository/            # Spring Data JPA repos
│  │     ├─ entity/                # JPA entities (User, Customer, Building, Role...)
│  │     ├─ security/
│  │     │  ├─ jwt/                # nếu có JWT
│  │     │  ├─ CustomUserDetailsService.java
│  │     │  ├─ CustomSuccessHandler.java
│  │     │  └─ SecurityUtils.java
│  │     ├─ config/                # WebSecurityConfig, CORS, Beans
│  │     ├─ converter/             # converters DTO <-> Entity
│  │     ├─ constant/              # SystemConstant.java
│  │     ├─ enums/                 # enum types (DistrictCode, BuildingType...)
│  │     └─ model/
│  │        └─ dto/                # MyUserDetail, BuildingDTO, etc.
│  ├─ resources/
│  │  ├─ templates/                # Thymeleaf templates
│  │  │  ├─ layouts/
│  │  │  ├─ admin/
│  │  │  └─ web/
│  │  ├─ static/                   # css, js, images
│  │  ├─ application.properties
│  │  └─ db/
│  │     └─ migration/             # Flyway SQL (nếu dùng)
│  └─ …
├─ test/                           # unit/integration tests
docs/                              # docs, PlantUML, ER diagrams, UserManual.docx
pom.xml (or mvnw, mvnw.cmd)        # build files
README.md

## Database (ER & Migration)
- Flyway dùng `src/main/resources/db/migration/*.sql`.
- Các bảng chính: user, role, user_role, customer, building, ...  
- Thêm file `docs/database/schema.sql` (nếu cần) và seed data.

## Tài liệu & Diagrams
- docs/UserManual.docx — tài liệu chi tiết + hướng dẫn phát triển
- docs/sequences/*.puml — PlantUML cho login, user-management, building flow
- docs/images/ — screenshots

## Thành viên nhóm
- Phan Duy Khương
- Phạm Huỳnh Khánh Linh
- Mai Hoàng Trúc Lâm
- Lục Nhật Khôi

## Demo / Hình ảnh

