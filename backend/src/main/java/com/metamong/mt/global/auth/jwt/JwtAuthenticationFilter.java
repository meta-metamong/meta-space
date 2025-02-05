package com.metamong.mt.global.auth.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.metamong.mt.global.config.constant.HttpRequestAuthorizationDefinition;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	@Autowired
	private final AuthenticationManager jwtAuthenticationManager;
	
	@Autowired
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;		
		
		// 헤더에서 JWT를 받아음.
		String token = jwtTokenProvider.resolveToken(httpRequest);
		
		// 유효한 토큰인지 확인.
		if (token != null) {						
			if(HttpRequestAuthorizationDefinition.NO_AUTH_REQUIRED_LIST.contains(httpRequest.getRequestURI())) {
				if(!"/api/members/reissue".equals(httpRequest.getRequestURI())) {
					httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					httpResponse.setCharacterEncoding("UTF-8");
	                httpResponse.setContentType("application/json");
	                httpResponse.getWriter().write("{\"message\": \"토큰 존재\"}");
	                return;
				}
				
				chain.doFilter(request, response);
				return;
			}
			
			// 토큰이 유효하면 토큰으로부터 사용자 정보를 받아옴
			Authentication authentication = jwtAuthenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(null, token));
			
			if(authentication == null) {
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"message\": \"유효하지 않은 토큰\"}");
                return;
			}
			// SecurityContext에 Authentication 객체를 저장.
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		
		chain.doFilter(request, response);
	}
}