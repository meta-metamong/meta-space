package com.metamong.mt.global;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(Integer.MIN_VALUE)
@Slf4j
public class ViewResolverFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        log.trace("URI requested={}", uri);
        if (uri.startsWith("/api")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/ws")
                || uri.equals("/")
                || uri.equals("/index.html")
                || uri.equals("/error")
                || uri.startsWith("/v3")
                || uri.startsWith("/actuator")
                || uri.startsWith("/assets")
                || uri.equals("/vite.svg")
                || uri.startsWith("/resources")) {
            chain.doFilter(httpRequest, response);
            return;
        }
        
        RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("/index.html");
        dispatcher.forward(httpRequest, response);
        return;
    }
}
