package com.metamong.mt.global.auth.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.metamong.mt.global.auth.jwt.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NoAuthPrincipalFinderFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getParameter("x-authorization");
        if (authorization == null) {
            authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.substring(0, "Bearer ".length()).toLowerCase().startsWith("bearer ")) {
                authorization = authorization.substring("Bearer ".length());
            }
        }
        
        if (authorization == null) {
            log.warn("authorization is null");
            filterChain.doFilter(request, response);
            return;
        }
        
        String memId = authorization;
        try {
            Long.valueOf(authorization);
        } catch (NumberFormatException e) {
            log.warn("Authorization is not memId. authorization={}", authorization);
            try {
                memId = this.jwtTokenProvider.getMemberId(authorization);
            } catch (Exception e2) {
                log.warn("JWT not valid, message={}", e2.getMessage());
                filterChain.doFilter(request, response);
                return;
            }
        }
        
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(memId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        log.info("authentication={}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("SecurityContextHolder.getContext()={}", SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
    }
}
