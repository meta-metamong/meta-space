package com.metamong.mt.global.jwt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    // 생성자를 통해 JwtTokenProvider 주입
    @Autowired
    public JwtHandshakeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

  

	@Override
	public boolean beforeHandshake(org.springframework.http.server.ServerHttpRequest request,
			org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		 String authHeader = request.getHeaders().getFirst("Authorization");  // HttpHeaders.AUTHORIZATION로 수정

	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            response.setStatusCode(HttpStatus.FORBIDDEN);
	            return false;  // 인증 실패
	        }

	        String token = authHeader.substring(7);  // "Bearer " 이후 토큰 부분만 추출
	        
	        // JWT 검증 로직
	        if (!jwtTokenProvider.validateToken(token)) {
	            response.setStatusCode(HttpStatus.FORBIDDEN);
	            return false;  // 인증 실패
	        }

	        return true;
	}

	@Override
	public void afterHandshake(org.springframework.http.server.ServerHttpRequest request,
			org.springframework.http.server.ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}
}
