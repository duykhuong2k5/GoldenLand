package com.javaweb.websocket;

import com.javaweb.model.request.ChatMessageRequest;
import com.javaweb.model.response.ChatMessageResponse;
import com.javaweb.service.impl.ChatServiceImpl;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatServiceImpl chatService;

    public ChatController(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/send")
    @SendTo("/topic/chat")
    public ChatMessageResponse handleChatMessage(@Payload ChatMessageRequest request) {
        return chatService.saveMessage(request);
    }
}
