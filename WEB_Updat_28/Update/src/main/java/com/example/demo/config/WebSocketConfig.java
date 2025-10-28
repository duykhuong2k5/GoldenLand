package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ✅ endpoint chính để client connect tới
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // cho phép tất cả origin
        // ⚠️ KHÔNG dùng .withSockJS() nếu bạn đang xài StompJS v7
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // ✅ các topic mà client có thể subscribe
        registry.enableSimpleBroker("/topic");
        // ✅ prefix cho các endpoint bên server (MessageMapping)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
