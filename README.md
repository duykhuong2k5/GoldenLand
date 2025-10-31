# 🏙️ GoldenLand

**Website quản lý bất động sản** – Dự án nhóm môn *Phát triển ứng dụng Web với Java Spring Boot.*

![GoldenLand Banner](https://dummyimage.com/1200x250/222/ffd700&text=GoldenLand+-+Real+Estate+Management)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)
![MySQL](https://img.shields.io/badge/Database-MySQL-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📑 Mục lục
- [🏠 Giới thiệu](#-giới-thiệu)
- [🎯 Mục tiêu hệ thống](#-mục-tiêu-hệ-thống)
- [⚙️ Chức năng chính](#️-chức-năng-chính)
- [🧩 Kiến trúc & Công nghệ](#-kiến-trúc--công-nghệ)
- [🧱 Yêu cầu môi trường](#-yêu-cầu-môi-trường)
- [⚙️ Cấu hình](#️-cấu-hình)
- [🗄️ Cơ sở dữ liệu & Migration](#️-cơ-sở-dữ-liệu--migration)
- [▶️ Cách chạy ứng dụng](#cách-chạy-ứng-dụng)
- [🐳 Docker Compose](#docker-compose)
- [👤 Tài khoản mẫu & Phân quyền](#tài-khoản-mẫu--phân-quyền)
- [🔗 API Docs](#api-docs)
- [📁 Cấu trúc thư mục](#cấu-trúc-thư-mục)
- [💡 Troubleshooting / FAQ](#troubleshooting--faq)
- [🤝 Đóng góp & License](#đóng-góp--license)
- [👥 Thành viên nhóm](#thành-viên-nhóm)
- [🖼️ Demo / Hình ảnh](#demo--hình-ảnh)

---

## 🏠 Giới thiệu
**GoldenLand** là hệ thống quản lý bất động sản toàn diện, hỗ trợ người dùng trong việc đăng tin, tìm kiếm, quản lý và giao dịch bất động sản một cách thuận tiện, minh bạch và hiệu quả.  
Dự án được xây dựng trên nền tảng **Java Spring Boot** với kiến trúc hiện đại, dễ mở rộng và dễ bảo trì, hướng đến mô hình **quản lý số hóa ngành bất động sản**.

Hệ thống cho phép nhiều nhóm người dùng tương tác thông qua một nền tảng thống nhất:
- 💼 **Quản lý tòa nhà, khách hàng và giao dịch** dành cho đội ngũ vận hành.  
- 🏘️ **Đăng tin, mua bán, cho thuê** dành cho khách hàng, chủ nhà hoặc doanh nghiệp bất động sản.  
- 🌐 **Tìm kiếm, xem chi tiết và so sánh** bất động sản cho người dùng khách (guest).

Ứng dụng tập trung vào 3 mục tiêu chính:
1. **Tối ưu hóa trải nghiệm người dùng:** Giao diện thân thiện, trực quan, dễ sử dụng.  
2. **Tự động hóa & bảo mật:** Áp dụng Spring Security, JWT, mã hóa BCrypt, xác thực email.  
3. **Mở rộng linh hoạt:** Tích hợp VNPAY, Cloudinary, Google Map API và Flyway Migration.

---

**Các nhóm người dùng:**
- 👑 **Admin:** Quản trị toàn bộ hệ thống, bao gồm quản lý người dùng, bài đăng, danh mục, dữ liệu và phân quyền.  
- 🧑‍💼 **Manager:** Quản lý người dùng và khách hàng, duyệt bài đăng, phân công công việc và quản lý các tòa nhà.  
- 👷 **Staff:** Hỗ trợ khách hàng và xử lý các tòa nhà, bài đăng được **Manager** giao.  
- 🧑‍💻 **Customer / Vendor:** Người dùng đã đăng ký, có thể đăng nhập để đăng bài, quản lý hồ sơ cá nhân và thực hiện thanh toán dịch vụ.  
- 👥 **Guest:** Người dùng chưa đăng ký hoặc đăng nhập, chỉ có thể xem thông tin bất động sản, tìm kiếm và xem chi tiết bài đăng.

---

## 🎯 Mục tiêu hệ thống
Phát triển website **full-stack Java Spring Boot** với các chức năng nổi bật:

- 🔐 **Phân quyền & xác thực:** Spring Security + JWT  
- 🖼️ **Upload ảnh:** Cloudinary  
- 💳 **Thanh toán:** VNPAY sandbox  
- 💬 **Chat thời gian thực:** Spring WebSocket + STOMP  
- 🗃️ **Migration schema:** Flyway  
- 🗺️ **Google Map API:** Hiển thị vị trí bất động sản  
- 🔒 **Bảo mật:** BCrypt PasswordEncoder, JWT  
- 🐳 **Triển khai:** Docker / Cloud  
- 📧 **Email:** Spring Mail (SMTP Gmail)

---

## ⚙️ Chức năng chính
| Chức năng | Vai trò | Mô tả |
|------------|----------|-------|
| 🔐 Đăng nhập / Phân quyền | Admin / Manager / Staff / Customer / Vendor | Xác thực người dùng, cấp token JWT và kiểm soát truy cập theo vai trò |
| 👥 CRUD Người dùng | Admin / Manager | Quản lý tài khoản, tạo, sửa, khóa, phân quyền người dùng |
| 🏢 CRUD Bất động sản | Admin / Manager / Staff / Customer / Vendor | Đăng, chỉnh sửa, duyệt, xóa bài đăng bất động sản tùy quyền hạn |
| 🧾 Quản lý hồ sơ khách hàng | Manager / Staff | Xem, chỉnh sửa và theo dõi thông tin khách hàng |
| ☁️ Upload ảnh Cloudinary | Customer / Vendor | Upload, cập nhật và xóa ảnh liên quan đến bài đăng |
| 🔑 Quên mật khẩu / Gửi Email | Tất cả | Gửi mail đặt lại mật khẩu hoặc thông báo quan trọng qua SMTP Gmail |
| 💳 Thanh toán VNPAY | Customer / Vendor | Thanh toán dịch vụ, bài đăng VIP hoặc gia hạn gói dịch vụ |
| 💬 Chat WebSocket | Manager / Staff | Chat nội bộ trong hệ thống, phân công và xử lý công việc theo thời gian thực |
| 👀 Xem và tìm kiếm bất động sản | Guest | Tìm kiếm, lọc, xem chi tiết bất động sản mà không cần đăng nhập |
| 🧱 Migration Schema (Flyway) | DevOps / Backend | Quản lý, cập nhật và đồng bộ cấu trúc cơ sở dữ liệu |

> 💡 *GoldenLand phân quyền rõ ràng giữa các nhóm người dùng, đảm bảo an toàn và tính minh bạch trong mọi thao tác hệ thống.*
---

## 🧩 Kiến trúc & Công nghệ
- **Ngôn ngữ:** Java 17  
- **Kiến trúc:** Layered Architecture (Controller – Service – Repository – Entity), tuân thủ mô hình MVC  
- **Backend:** Spring Boot (Spring MVC, Spring Security, Spring Data JPA, WebSocket, Validation)  
- **Application Server:** Embedded **Apache Tomcat** (tích hợp trong Spring Boot)  
- **Frontend:** Thymeleaf, Bootstrap 5, jQuery, AJAX (hoặc React tùy phiên bản mở rộng)  
- **Database:** MySQL 8.x + Flyway (migration tự động)  
- **Upload:** Cloudinary (lưu trữ và quản lý hình ảnh bất động sản)  
- **Thanh toán:** VNPAY Sandbox (tích hợp cổng thanh toán online)  
- **Email:** Spring Mail (SMTP Gmail – gửi thông báo, xác thực, quên mật khẩu)  
- **API Documentation:** SpringDoc OpenAPI + Swagger UI  
- **Bảo mật:** JWT Authentication, BCrypt PasswordEncoder, Spring Security  
- **Build tool:** Maven (quản lý dependencies & build project)  
- **Logging:** SLF4J + Logback (ghi log hệ thống)  
- **Triển khai / Container:** Docker, Docker Compose  
- **Version control:** Git & GitHub  
- **IDE:** IntelliJ IDEA / VS Code  
- **Thiết kế & sơ đồ:** PlantUML (Use Case, ERD, Flow Diagram)
- 
---

## 🧱 Yêu cầu môi trường
- ☕ **Java JDK:** 17+  
- 🧩 **MySQL Server:** 8+  
- 🛠️ **Maven:** 3.9+
- 🐱‍💻 Application Server: Embedded Tomcat (có sẵn trong Spring Boot)
- 🌐 **Tùy chọn:**  
  - Cloudinary account  
  - Gmail App Password  
  - VNPAY Sandbox Keys  
---

## ⚙️ Cấu hình
File: `src/main/resources/application.properties`

```properties
# Tên ứng dụng
spring.application.name=CRUD13-9

# JDBC Driver MySQL
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/estateadvance?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=Khuowng205@#

# JPA / Hibernate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=none

# Thymeleaf
spring.thymeleaf.cache=false
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML


#Mailsender
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=lamssdd910@gmail.com
spring.mail.password=znqymqttykjkzmau
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

spring.http.encoding.charset=UTF-8
spring.http.encoding.enabled=true
spring.http.encoding.force=true
# Server
server.port=8092
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.enabled=true
server.servlet.encoding.force=true


# Logging
logging.level.org.thymeleaf=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
# Cloudinary
cloudinary.cloud_name=dnbxsm1mx
cloudinary.api_key=329513356252861
cloudinary.api_secret=PC_sIT6yaw-3fWy9jFKsMMbKTHA

# ==========================
# VNPAY SANDBOX CONFIG
# ==========================
vnpay.tmnCode=E782UYRX
vnpay.hashSecret=URJVND7UIEXNLZRKIQ50XDOYFRB10G0F
vnpay.payUrl=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=http://localhost:8092/vnpay_return


chat.staff.ids=2,3,4
chat.staff.max-per-staff=1

#chat.staff.start-index=0
logging.level.org.springframework.web.socket=DEBUG
logging.level.org.springframework.messaging=DEBUG
logging.level.org.springframework.web.socket.messaging=TRACE
# ==========================
# FLYWAY DATABASE MIGRATION
# ==========================
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
# Nếu DB đã có sẵn dữ liệu (vd. building, customer,...)
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=0



🗄️ Cơ sở dữ liệu & Migration

Migration: src/main/resources/db/migration

File mẫu: V3__seed_sample_data.sql

ERD: docs/database/ERD.png

Bảng chính: user, role, user_role, customer, building, payment

## ▶️ Cách chạy ứng dụng

# Tạo database
CREATE DATABASE estateadvance CHARACTER SET utf8mb4;

# Build & run
mvn clean package
mvn spring-boot:run

# Truy cập
http://localhost:8092
---
## 🐳 Docker Compose
yaml
Copy code
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
## 👤 Tài khoản mẫu & Phân quyền
Vai trò	Email	Mật khẩu	Quyền
🛠️ Admin	admin@example.com	123456	Toàn quyền
👨‍💼 Manager	manager@gl.com	123456	Duyệt bài, phân công
👩‍💻 Staff	vanu123@gmailcom	123456	Quản lý khách hàng
👥 Customer		handuykhuong05012005@gmail.com khuong1234	Đăng bài, thanh toán

🔗 API Docs
Swagger UI → http://localhost:8092/swagger-ui/index.html
OpenAPI JSON → /v3/api-docs

---
## 📁 Cấu trúc thư mục
src/
├─ main/
│ ├─ java/com/example/demo/
│ │ ├─ api/ # Xử lý API REST, request/response DTO
│ │ ├─ builder/ # Xây dựng dữ liệu phức tạp, hỗ trợ tạo object
│ │ ├─ config/ # Cấu hình ứng dụng (Spring, Security, WebSocket, Mail,…)
│ │ ├─ constant/ # Các hằng số (constants) dùng chung toàn hệ thống
│ │ ├─ controller/ # Xử lý request từ người dùng (Web + API)
│ │ ├─ converter/ # Chuyển đổi giữa entity ↔ DTO
│ │ ├─ entity/ # Các entity ánh xạ với bảng trong cơ sở dữ liệu
│ │ ├─ enums/ # Các enum dùng cho trạng thái, loại role, loại bất động sản, v.v.
│ │ ├─ exception/ # Xử lý ngoại lệ, error handler
│ │ ├─ model/ # Các lớp DTO (Data Transfer Object)
│ │ ├─ repository/ # Tầng truy xuất dữ liệu (DAO layer)
│ │ ├─ security/ # Cấu hình bảo mật, JWT, phân quyền, filter, v.v.
│ │ ├─ service/ # Tầng nghiệp vụ, xử lý logic chính của hệ thống
│ │ ├─ utils/ # Tiện ích (format, mã hóa, validate,…)
│ │ └─ Crud139Application.java # File main để chạy ứng dụng Spring Boot
│ │
│ └─ resources/
│ ├─ db/
│ │ └─ migration/ # File migration quản lý bởi Flyway
│ ├─ fonts/ # Font dùng cho báo cáo hoặc giao diện
│ ├─ i18n/ # File đa ngôn ngữ (Internationalization)
│ ├─ static/
│ │ └─ images/ # Ảnh tĩnh, favicon, logo, banner, v.v.
│ ├─ templates/
│ │ ├─ admin/
│ │ │ ├─ building/ # Giao diện quản lý tòa nhà
│ │ │ ├─ customer/ # Giao diện quản lý khách hàng
│ │ │ ├─ market/ # Giao diện quản lý thị trường / bài đăng
│ │ │ └─ user/ # Giao diện quản lý người dùng
│ │ ├─ layouts/ # Template layout chung cho admin và web
│ │ └─ web/
│ │ ├─ home.html
│ │ ├─ forgot-password.html
│ │ ├─ login.html
│ │ └─ reset-password.html
│ └─ application.properties # File cấu hình chính của ứng dụng
│
└─ test/java/com/example/demo/
└─ ... # Test unit & integration
docs/
├─ images/ # Ảnh minh họa, UI demo
├─ database/ # ERD, mô hình dữ liệu

> 🧭 **GoldenLand** tuân theo kiến trúc nhiều lớp (multi-layered architecture), giúp dễ mở rộng, bảo trì và tích hợp với các dịch vụ ngoài như Cloudinary, VNPAY và Google API.
>  
> Các thư mục được tổ chức rõ ràng theo chuẩn dự án Spring Boot hiện đại, hỗ trợ cả MVC và RESTful API.

---
## 💡 Troubleshooting / FAQ

| Vấn đề | Nguyên nhân | Giải pháp |
|--------|--------------|-----------|
| ❌ **Gmail 535-5.7.8** | Sai App Password | Tạo App Password mới trong tài khoản Google |
| ⚠️ **MySQL Public Key Retrieval** | Cấu hình chưa bật retrieval | Thêm `allowPublicKeyRetrieval=true` vào JDBC URL |
| 💳 **Sai checksum VNPAY** | Sai `hashSecret` hoặc lỗi encode URL | Kiểm tra lại file `application.properties` và cấu hình VNPAY |
| ☁️ **Cloudinary 401 Unauthorized** | Sai `api_key` hoặc `api_secret` | Kiểm tra cấu hình trong `application.properties` |
| 🧭 **Lỗi mapping hoặc migration** | Flyway chưa đồng bộ | Chạy `mvn clean` và `mvn spring-boot:run` lại để migrate |
| 🔐 **Lỗi đăng nhập JWT** | Token hết hạn hoặc sai header | Kiểm tra header `Authorization` trong request |
| 🐳 **Docker không khởi động** | Port hoặc DB container bị trùng | Dừng container cũ: `docker stop <container>` rồi chạy lại |
| 🌐 **Không load CSS / JS** | Thiếu mapping static resources | Kiểm tra lại `spring.web.resources.static-locations` trong config |

> 💡 *Nếu gặp lỗi khác, kiểm tra file `application.properties` hoặc console log để xác định nguyên nhân cụ thể.*

---

## 🤝 Đóng góp & License

### 🧭 Quy ước commit
Sử dụng chuẩn commit message để dễ quản lý lịch sử:
- `feat:` thêm mới tính năng  
- `fix:` sửa lỗi  
- `docs:` cập nhật tài liệu  
- `refactor:` cải thiện code mà không thay đổi logic  

### 🌿 Nhánh làm việc
- `main` → Nhánh ổn định (production)  
- `dev` → Nhánh phát triển chung  
- `feature/*` → Nhánh chức năng riêng

### 🪪 Giấy phép
Dự án được phát hành theo giấy phép [MIT License](./LICENSE).  
Bạn có thể tự do sử dụng, chỉnh sửa và phân phối lại với điều kiện giữ nguyên ghi chú bản quyền.

> 🧡 *Đóng góp, báo lỗi hoặc đề xuất cải tiến luôn được hoan nghênh qua Pull Request hoặc Issue!*

---
## 👥 Thành viên nhóm

| Họ tên | Vai trò & Nhiệm vụ chính | Khu vực / Module phụ trách |
|--------|----------------------------|-----------------------------|
| 🧑‍💻 **Phan Duy Khương** | - Quản trị người dùng (**Admin**)  <br> - Bảo mật & phân quyền (**RBAC**)  <br> - Phát triển tính năng **Manager & Staff**  <br> - **CRUD Building**, lịch sử giá tòa nhà  <br> - Tích hợp **Thanh toán VNPay**  <br> - Quản lý **Migration Schema (Flyway)**  <br> - Giao diện: *Quên mật khẩu*, *So sánh tòa nhà*, *Trang sản phẩm*  <br> - Hỗ trợ bảo mật: **JWT Authentication** *(phụ thành viên 2)* | `templates/admin/building/` |
| 👩‍💻 **Phạm Huỳnh Khánh Linh** | - Quản lý hình ảnh tòa nhà (**Cloudinary**)  <br> - Tích hợp **Google Map** (trang chi tiết bất động sản)  <br> - Xây dựng luồng **Vendor flows + My Posts**  <br> - **CRUD User (Customer)**  <br> - **Tìm kiếm nâng cao** & **Review (Đánh giá tòa nhà)**  <br> - Bảo mật: **JWT Authentication** | `templates/admin/customer/` |
| 🧑‍💼 **Mai Hoàng Trúc Lâm** | - **Đăng ký, OTP & Đăng nhập** (qua email)  <br> - Quản lý **hồ sơ cá nhân** & **đổi mật khẩu**  <br> - Phát triển **Realtime Chatbox (WebSocket)**  <br> - Xây dựng **phân trang danh sách**  <br> - Kết nối module **My Posts** (liên kết Member 2) | `templates/admin/user/` |
| 👨‍💼 **Lục Nhật Khôi** | - Thiết kế & phát triển **Dashboard UI**  <br> - Phối hợp **Thanh toán VNPay** (cùng Member 1)  <br> - Xây dựng giao diện **layouts chung**  <br> - Quản lý giao diện người dùng: **templates/web/** | `templates/layouts/`, `templates/web/` |

---
## 🖼️ Demo / Hình ảnh
> Thư mục hình ảnh: `docs/images/`

| Trang chủ | Quản lý bài đăng | Thanh toán VNPay |
|------------|------------------|------------------|
| ![Trang chủ](docs/images/home.png) | ![Quản lý bài đăng](docs/images/building.png) | ![Thanh toán VNPay](docs/images/payment.png) |

| Google Map | Chatbox Realtime | Dashboard |
|-------------|-----------------|------------|
| ![Google Map](docs/images/map.png) | ![Chatbox](docs/images/chatbox.png) | ![Dashboard](docs/images/dashboard.png) |


💛 GoldenLand – Giải pháp quản lý bất động sản thông minh, an toàn và tiện lợi.
