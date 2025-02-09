package com.metamong.mt.global.logging;

import java.io.BufferedInputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "anonymous";
        Collection<?> authorities = List.of();
        if (authentication != null) {
            username = authentication.getName();
            authorities = authentication.getAuthorities();
        }
        log.info("(client user id: {}, authorities: {}) {} {}", username, authorities, request.getMethod(), request.getRequestURI());
        
        if (log.isDebugEnabled()) {
            log.debug("[[[[[[[[[[[[[[]]]]]]]]]]]]]]");
            log.debug("Request Params:");
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                log.debug("{}: {}", paramName, request.getParameter(paramName));
            }
            log.debug("[[[[[[[[[[[[[[]]]]]]]]]]]]]]");
        }
        return true;
    }
}
