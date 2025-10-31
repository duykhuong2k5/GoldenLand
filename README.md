<div align="center">

# 🏙️ GoldenLand
### *Real Estate Management Platform*

![Banner](https://dummyimage.com/1000x220/222/ffd700&text=GoldenLand+-+Property+Management)

[![Java](https://img.shields.io/badge/Java-17-blue?style=flat-square&logo=java&logoColor=white)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green?style=flat-square&logo=springboot&logoColor=white)]()
[![MySQL](https://img.shields.io/badge/Database-MySQL-orange?style=flat-square&logo=mysql&logoColor=white)]()
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)]()

</div>

---

> 💼 **GoldenLand** là ứng dụng quản lý bất động sản giúp người dùng đăng, tìm kiếm, và giao dịch hiệu quả – được xây dựng bằng **Java Spring Boot 3.0**, tích hợp **VNPAY, Cloudinary, Flyway** và **WebSocket Realtime Chat**.
---


## 📑 Mục lục
- [🏠 Giới thiệu](#-giới-thiệu)
- [🎯 Mục tiêu hệ thống](#-mục-tiêu-hệ-thống)
- [⚙️ Chức năng chính](#️-chức-năng-chính)
- [🧩 Kiến trúc & Công nghệ](#-kiến-trúc--công-nghệ)
- [🧱 Yêu cầu môi trường](#-yêu-cầu-môi-trường)
- [⚙️ Cấu hình](#️-cấu-hình)
- [🗄️ Cơ sở dữ liệu & Migration](#️-cơ-sở-dữ-liệu--migration)
- [▶️ Cách chạy ứng dụng](#-cách-chạy-ứng-dụng)
- [🐳 Docker Compose](#-docker-compose)
- [👤 Tài khoản mẫu & Phân quyền](#-tài-khoản-mẫu--phân-quyền)
- [🔗 API Docs](#-api-docs)
- [📁 Cấu trúc thư mục](#-cấu-trúc-thư-mục)
- [💡 Troubleshooting / FAQ](#-troubleshooting--faq)
- [🤝 Đóng góp & License](#-đóng-góp--license)
- [👥 Thành viên nhóm](#-thành-viên-nhóm)
- [🖼️ Demo / Hình ảnh](#-demo--hình-ảnh)

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
- 👑 **Admin:** QWebsocket, Hồ sơ cá nhân, Phân trang** | `templates/admin/user/` | 🧑‍💼 **Mai Hoàng Trúc Lâm** |
| `Khuong_Tonghop` | ⚙️ Tích hợp và kiểm thử: **Bảo mật & phân quyền (RBAC)**,**Quản trị người dùng (Admin)**,**Building CRUD**, **Manager & Staff**, **VNPay**, **Flyway Migration**, **JWT Auth** | `templates/admin/building/` | 🧑‍💻 **Phan Duy Khương** |

---

> 🔁 **Quy trình làm việc:**  
> Mỗi thành viên phát triển tính năng riêng trên nhánh cá nhân → đẩy lên GitHub →  
> Khương (Lead) kiểm thử, sau đó merge vào `main`.  
>  
> 🧠 *Quy ước nhánh:*  
> - `main`: bản ổn định cuối cùng  
> - `feature/<tên-module>`: nhánh phụ khi cần mở rộng  
> - `hotfix/<mô-tả>`: dành cho sửa lỗi khẩn

> 🧡 *Mọi đóng góp, báo lỗi hoặc đề xuất cải tiến luôn được hoan nghênh qua Pull Request hoặc Issue!*

---

## 👥 Thành viên nhóm

| 👤 **Họ tên** | 💼 **Vai trò & Nhiệm vụ chính** |
|:--------------|:--------------------------------|
| 🧑‍💻 **Phan Duy Khương** | - Quản trị người dùng (**Admin**) <br> - Bảo mật & phân quyền (**RBAC**) <br> - Phát triển tính năng **Manager & Staff** <br> - **CRUD Building**, lịch sử giá tòa nhà <br> - Tích hợp **Thanh toán VNPay** <br> - Quản lý **Migration Schema (Flyway)** <br> - Giao diện: *Quên mật khẩu*, *So sánh tòa nhà*, *Trang sản phẩm* <br> - Hỗ trợ bảo mật: **JWT Authentication** *(phụ thành viên 2)* <br> - 📂 **Khu vực:** `templates/admin/building/` |
| 👩‍💻 **Phạm Huỳnh Khánh Linh** | - Quản lý hình ảnh tòa nhà (**Cloudinary**) <br> - Tích hợp **Google Map** (trang chi tiết bất động sản) <br> - Xây dựng luồng **Vendor flows + My Posts** <br> - **CRUD User (Customer)** <br> - **Tìm kiếm nâng cao** & **Review (Đánh giá tòa nhà)** <br> - Bảo mật: **JWT Authentication** <br> - 📂 **Khu vực:** `templates/admin/customer/` |
| 🧑‍💼 **Mai Hoàng Trúc Lâm** | - **Đăng ký, OTP & Đăng nhập** (qua email) <br> - Quản lý **hồ sơ cá nhân** & **đổi mật khẩu** <br> - Phát triển **Realtime Chatbox (WebSocket)** <br> - Xây dựng **phân trang danh sách** <br> - Kết nối module **My Posts** (liên kết Member 2) <br> - 📂 **Khu vực:** `templates/admin/user/` |
| 👨‍💼 **Lục Nhật Khôi** | - Thiết kế & phát triển **Dashboard UI** <br> - Phối hợp **Thanh toán VNPay** (cùng Member 1) <br> - Xây dựng giao diện **layouts chung** <br> - Quản lý giao diện người dùng web <br> - 📂 **Khu vực:** `templates/layouts/`, `templates/web/` |

---

## 🖼️ Demo / Hình ảnh

> 🗂️ Thư mục hình ảnh: `docs/images/`

| 🏠 **Trang chủ** | 🏢 **Quản lý bài đăng** | 💳 **Thanh toán VNPay** |
|:----------------:|:----------------------:|:----------------------:|
| ![Trang chủ](docs/images/home.png) | ![Quản lý bài đăng](docs/images/building.png) | ![Thanh toán VNPay](docs/images/payment.png) |

| 🗺️ **Google Map** | 💬 **Chatbox Realtime** | 📊 **Dashboard** |
|:------------------:|:----------------------:|:----------------:|
| ![Google Map](docs/images/map.png) | ![Chatbox](docs/images/chatbox.png) | ![Dashboard](docs/images/dashboard.png) |

---

💛 **GoldenLand** – *Giải pháp quản lý bất động sản thông minh, an toàn và tiện lợi.*
