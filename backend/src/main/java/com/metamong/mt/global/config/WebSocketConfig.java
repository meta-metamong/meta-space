package com.metamong.mt.global.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.metamong.mt.domain.notification.handler.ClientResolverHandshakeInterceptor;
import com.metamong.mt.domain.notification.handler.NotificationWebSocketHandler;
import com.metamong.mt.domain.notification.service.WebSocketNotificationService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Value("${client.origin}")
    private String clientOrigin;
    
    @Autowired
    private WebSocketNotificationService webSocketNotificationService;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // "/ws" 엔드포인트로 WebSocket 핸들러를 등록
        registry.addHandler(new NotificationWebSocketHandler(this.webSocketNotificationService), "/ws")
                .setAllowedOrigins(this.clientOrigin)  // Cross-Origin 설정
                .addInterceptors(new ClientResolverHandshakeInterceptor());  // JWT 인증 인터셉터 추가
    }



//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/app");
//    }
}
