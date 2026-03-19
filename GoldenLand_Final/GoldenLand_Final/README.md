# GoldenLand

## Mô tả dự án

GoldenLand là một ứng dụng web quản lý bất động sản được xây dựng bằng Spring Boot. Dự án cung cấp nền tảng cho việc quản lý tòa nhà, khách hàng, thị trường bất động sản, với các tính năng như đăng ký/đăng nhập, chat real-time với nhân viên hỗ trợ, thanh toán online, và tích hợp bản đồ Google Maps để xem vị trí.

## Công nghệ chính

- **Ngôn ngữ lập trình**: Java 22
- **Framework**: Spring Boot 3.5.5
- **Template Engine**: Thymeleaf
- **Database**: MySQL (với Flyway cho migration), H2 (cho testing)
- **Security**: Spring Security
- **WebSocket**: Cho chat real-time
- **ORM**: Spring Data JPA với Hibernate
- **Build Tool**: Maven

## Thư viện và Dependencies

- **Spring Boot Starters**:
  - spring-boot-starter-data-jpa: Quản lý dữ liệu với JPA
  - spring-boot-starter-mail: Gửi email
  - spring-boot-starter-validation: Validation dữ liệu
  - spring-boot-starter-web: Web MVC
  - spring-boot-starter-websocket: WebSocket cho chat
  - spring-boot-starter-security: Bảo mật ứng dụng

- **Database**:
  - mysql-connector-j: Kết nối MySQL
  - h2: Database trong bộ nhớ cho testing
  - flyway-mysql: Migration database

- **Template và UI**:
  - thymeleaf-extras-springsecurity6: Tích hợp Thymeleaf với Spring Security
  - thymeleaf-layout-dialect: Layout cho Thymeleaf
  - tomcat-embed-jasper: JSP support
  - jakarta.servlet-api, jakarta.servlet.jsp.jstl: Servlet và JSTL

- **Utilities**:
  - lombok: Giảm boilerplate code
  - modelmapper: Mapping object
  - commons-lang: Utilities cho String, v.v.
  - pdfbox: Xử lý PDF

- **Security và Authentication**:
  - spring-security-crypto: Mã hóa
  - spring-security-taglibs: Tags cho Thymeleaf
  - jjwt: JSON Web Tokens

- **Logging**: log4j

- **Cloud Services**:
  - cloudinary-http44: Upload và quản lý ảnh trên cloud
  - apache-httpclient: HTTP client cho Cloudinary

- **Other**:
  - displaytag: Hiển thị bảng dữ liệu

## API và Dịch vụ ngoài

- **VNPay Payment Gateway**: Tích hợp thanh toán online (Sandbox mode)
  - URL: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
  - Return URL: http://localhost:8092/vnpay_return

- **Google Maps JavaScript API**: Hiển thị bản đồ và Places

- **Cloudinary**: Lưu trữ và quản lý hình ảnh

- **Gmail SMTP**: Gửi email
  - Host: smtp.gmail.com
  - Port: 587

## Cấu hình

- **Server Port**: 8092
- **Database**: MySQL trên localhost:3306, database `estateadvance`
- **Flyway**: Enabled, baseline version 0

## Chạy dự án

1. Đảm bảo có Java 22 và Maven đã cài đặt.
2. Cấu hình MySQL database với thông tin trong `application.properties`.
3. Chạy lệnh: `mvn spring-boot:run`
4. Truy cập: http://localhost:8092

## Tính năng chính

- Quản lý tòa nhà (CRUD)
- Quản lý khách hàng và người dùng
- Thị trường bất động sản
- Chat real-time với nhân viên
- Thanh toán online
- Upload ảnh qua Cloudinary
- Đa ngôn ngữ (Tiếng Việt, Tiếng Anh)
- Bảo mật với Spring Security và JWT
## Đăng nhập
- admin : nguyenvana/123456
- customer : khuong123/khuong1234