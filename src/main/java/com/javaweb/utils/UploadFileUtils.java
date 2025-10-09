package com.javaweb.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFileUtils {
    // Hàm upload file lên Cloudinary (mock)
    public String uploadToCloudinary(MultipartFile file) {
        // TODO: Tích hợp Cloudinary SDK thực tế
        return "https://res.cloudinary.com/demo/image/upload/sample.jpg";
    }
}
