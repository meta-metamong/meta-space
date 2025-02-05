package com.metamong.mt.global.auth;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.auth.jwt.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager{

	private final JwtTokenProvider jwtTokenProvider;

	private final UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = (String)authentication.getCredentials();
		if(!this.validateToken(token)) {
			return null;
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getMemberId(token));
		
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	/**
     * 토큰의 유효성과 만료 일자 확인
     * @param token 토큰
     * @return 토큰이 유효한지 확인, 유효하면 true 반환
     */
    public boolean validateToken(String token) {    	
        try {
        	Claims claim = jwtTokenProvider.getClaims(token);
        	UserDetails userDetails = userDetailsService.loadUserByUsername(claim.getSubject());
            return claim.getSubject().equals(userDetails.getUsername()) && !claim.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
