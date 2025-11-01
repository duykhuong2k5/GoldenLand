package com.example.demo.model.response;

import lombok.*;

import java.util.Date;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class MyPostVM {
    private Long   id;
    private String name;
    private String district;
    private String ward;
    private String status;
    private String imageUrl;

    private Long   views;         // viewCount từ Building
    private Double avgRating;     // trung bình sao
    private Long   reviewCount;   // số lượt đánh giá
    private Long   revenue;       // doanh thu của bài

    private Date   createdDate;
    private Date   modifiedDate;
}
