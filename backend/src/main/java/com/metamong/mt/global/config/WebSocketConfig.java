package com.metamong.mt.global.config;


import com.metamong.mt.global.jwt.JwtHandshakeInterceptor;
import com.metamong.mt.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final MyWebSocketHandler myWebSocketHandler;

    public WebSocketConfig(JwtTokenProvider jwtTokenProvider, MyWebSocketHandler myWebSocketHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myWebSocketHandler = myWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // "/ws" 엔드포인트로 WebSocket 핸들러를 등록
        registry.addHandler(myWebSocketHandler, "/ws")
                .setAllowedOrigins("*")  // Cross-Origin 설정
                .addInterceptors(new JwtHandshakeInterceptor(jwtTokenProvider));  // JWT 인증 인터셉터 추가
    }



//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/app");
//    }
}
