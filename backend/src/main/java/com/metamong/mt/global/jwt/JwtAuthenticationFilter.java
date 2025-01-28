package com.metamong.mt.global.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

	 private final JwtTokenProvider jwtTokenProvider;

	    // JwtTokenProvider 주입
	    @Autowired
	    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
	        this.jwtTokenProvider = jwtTokenProvider;
	    }

	    // 요청 처리
	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	        // 요청 헤더에서 JWT 토큰을 가져옴
	        String token = jwtTokenProvider.resolveToken(request);
	        
	        // 토큰이 유효하면 인증 정보를 SecurityContext에 설정
	        if (token != null && jwtTokenProvider.validateToken(token)) {
	            SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
	        }
	        
	        // 필터 체인 계속 진행
	        filterChain.doFilter(request, response);
	    }
}