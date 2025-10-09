package com.javaweb.model.response;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private String type;
    private String mediaUrl;
    private LocalDateTime sentAt;
    // getters/setters
}
