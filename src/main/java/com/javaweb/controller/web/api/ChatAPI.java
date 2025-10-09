package com.javaweb.controller.web.api;

import com.javaweb.model.request.ChatMessageRequest;
import com.javaweb.model.response.ChatMessageResponse;
import com.javaweb.service.impl.ChatServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatAPI {

    private final ChatServiceImpl chatService;

    public ChatAPI(ChatServiceImpl chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponse> sendMessage(@Valid @RequestBody ChatMessageRequest request) {
        return ResponseEntity.ok(chatService.saveMessage(request));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<List<ChatMessageResponse>> getConversation(@PathVariable Long transactionId) {
        return ResponseEntity.ok(chatService.getConversation(transactionId));
    }
}
