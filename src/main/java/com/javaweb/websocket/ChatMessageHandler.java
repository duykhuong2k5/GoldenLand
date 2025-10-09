package com.javaweb.websocket;

import com.javaweb.model.response.ChatMessageResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Object payload = message.getPayload();
        if (payload instanceof ChatMessageResponse response) {
            accessor.setNativeHeader("transactionId", String.valueOf(response.getTransactionId()));
        }
        return message;
    }
}
