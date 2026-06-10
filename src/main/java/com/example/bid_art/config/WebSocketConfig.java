package com.example.bid_art.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 경로: 클라이언트는 /topic/items/{id} 를 구독해 실시간 입찰을 받는다.
        registry.enableSimpleBroker("/topic");
        // 발행 경로: 클라이언트 -> 서버 메시지 prefix (현재는 REST로 입찰을 받으므로 예약 용도)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 네이티브 WebSocket 핸드셰이크 엔드포인트 (프론트엔드 Vite 주소 허용)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:5173");
    }
}
