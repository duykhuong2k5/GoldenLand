package com.javaweb.utils;

import java.io.InputStream;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFileUtils {

    public String uploadReviewMedia(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return String.format("https://res.cloudinary.com/demo/image/upload/%s-%s", UUID.randomUUID(), file.getOriginalFilename());
    }

    public String uploadReviewMedia(InputStream inputStream, String originalFilename) {
        if (inputStream == null) {
            return null;
        }
        return String.format("https://res.cloudinary.com/demo/image/upload/%s-%s", UUID.randomUUID(), originalFilename);
    }
}
