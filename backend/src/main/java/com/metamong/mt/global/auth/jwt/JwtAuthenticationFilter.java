package com.metamong.mt.global.auth.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.metamong.mt.global.config.constant.HttpRequestAuthorizationDefinition;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
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
		if (log.isDebugEnabled()) {
		    log.debug("token={}", token);
		}
		
		// 유효한 토큰인지 확인.
		if (token != null) {						
//			if(HttpRequestAuthorizationDefinition.NO_AUTH_REQUIRED_LIST.contains(httpRequest.getRequestURI())) {
//				if(!"/api/members/reissue".equals(httpRequest.getRequestURI())) {
//					this.responseForUnauthorizedToken(httpResponse, "토큰 존재");
//	                return;
//				}
//				
//				chain.doFilter(request, response);
//				return;
//			}
			
			try {
				if(jwtTokenProvider.validateToken(token)) {
					// 토큰이 유효하면 토큰으로부터 사용자 정보를 받아옴
					Authentication authentication = jwtAuthenticationManager.authenticate(
							new UsernamePasswordAuthenticationToken(null, token));
					// SecurityContext에 Authentication 객체를 저장.
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}catch (ExpiredJwtException e) {
				this.responseForUnauthorizedToken(httpResponse, "만료된 토큰");
                return;
	        } catch (SecurityException | MalformedJwtException e) {
	        	this.responseForUnauthorizedToken(httpResponse, "유효하지 않은 토큰");
	        	return;
	        } catch(Exception e) {
	            this.responseForUnauthorizedToken(httpResponse, "잘못된 토큰");
	            return;
	        }
		}
		
		
		chain.doFilter(request, response);
	}
	
	private void responseForUnauthorizedToken(HttpServletResponse httpResponse, String message) throws IOException{
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json");
        
        StringBuilder responseMessage = new StringBuilder();
        responseMessage.append("{\"message\": \"");
        responseMessage.append(message);
        responseMessage.append("\"}");
        httpResponse.getWriter().write(responseMessage.toString());
	}
}