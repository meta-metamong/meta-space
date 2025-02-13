package com.metamong.mt.domain.notification.handler;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ClientResolverHandshakeInterceptor implements HandshakeInterceptor {
    
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        HttpServletRequest httpRequest = ((ServletServerHttpRequest)request).getServletRequest();
        try {
            Long memId = Long.valueOf(httpRequest.getParameter("mem-id"));
            log.info("memId={}", memId);
            attributes.put("memId", Long.valueOf(memId));
            return true;
        } catch (NullPointerException | NumberFormatException e) {
            log.warn("Can't establish WebSocket connection", e);
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception ex) {
    }
}

