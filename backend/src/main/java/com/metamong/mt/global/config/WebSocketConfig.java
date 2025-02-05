package com.metamong.mt.global.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.metamong.mt.global.auth.JwtAuthenticationManager;
import com.metamong.mt.global.auth.jwt.JwtHandshakeInterceptor;
import com.metamong.mt.global.auth.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final MyWebSocketHandler myWebSocketHandler;
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // "/ws" 엔드포인트로 WebSocket 핸들러를 등록
        registry.addHandler(myWebSocketHandler, "/ws")
                .setAllowedOrigins("*")  // Cross-Origin 설정
                .addInterceptors(new JwtHandshakeInterceptor(jwtTokenProvider, jwtAuthenticationManager));  // JWT 인증 인터셉터 추가
    }



//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/app");
//    }
}
