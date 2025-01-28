package com.metamong.mt.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.metamong.mt.global.jwt.JwtHandshakeInterceptor;
import com.metamong.mt.global.jwt.JwtTokenProvider;

@Configuration
@EnableWebSocketMessageBroker  // WebSocket 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {  // WebSocketMessageBrokerConfigurer 인터페이스를 구현

    private final JwtTokenProvider jwtTokenProvider;

    // JwtTokenProvider를 생성자를 통해 주입
    public WebSocketConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트 "/ws" 등록
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")  // 모든 출처에서 연결을 허용
                .addInterceptors(new JwtHandshakeInterceptor(jwtTokenProvider))  // JWT 인증 인터셉터 추가
                .withSockJS();  // SockJS 사용 (웹소켓을 지원하지 않는 브라우저를 위한 폴백)
        

    }


}
