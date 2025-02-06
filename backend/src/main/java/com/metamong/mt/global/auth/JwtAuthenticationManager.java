package com.metamong.mt.global.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.metamong.mt.global.auth.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager{

	private final JwtTokenProvider jwtTokenProvider;

	private final UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = (String)authentication.getCredentials();
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getMemberId(token));
		if(!jwtTokenProvider.getClaims(token).getSubject().equals(userDetails.getUsername())) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
}
