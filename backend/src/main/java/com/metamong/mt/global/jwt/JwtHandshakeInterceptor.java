package com.metamong.mt.global.jwt;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.Cookie;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

        String token = null;
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
            String username = jwtTokenProvider.getUsernameFromToken(token);
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

