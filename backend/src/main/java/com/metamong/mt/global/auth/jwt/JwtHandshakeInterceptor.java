package com.metamong.mt.global.auth.jwt;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.metamong.mt.global.auth.JwtAuthenticationManager;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

        String token = null;
        System.out.println("연결시도");
        if (servletRequest.getServletRequest().getCookies() != null) {
            for (Cookie cookie : servletRequest.getServletRequest().getCookies()) {
                if ("Ws-Access-Token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            return false;  
        }

        if (jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            attributes.put("username", username);  
            return true;
        }

        return false;  
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception ex) {
    }
}

