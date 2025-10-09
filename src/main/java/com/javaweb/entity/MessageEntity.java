package com.javaweb.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private String type; // text, image, video
    private String mediaUrl; // nếu là hình/video
    private LocalDateTime sentAt;
    // getters/setters
}
