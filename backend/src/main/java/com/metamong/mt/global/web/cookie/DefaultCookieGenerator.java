package com.metamong.mt.global.web.cookie;

import org.springframework.http.ResponseCookie;

public class DefaultCookieGenerator implements CookieGenerator {
    private final String clientDomain;
    
    public DefaultCookieGenerator(String clientDomain) {
        this.clientDomain = clientDomain;
    }

    @Override
    public ResponseCookie generateCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path(CookieGenerator.PATH)
                .maxAge(CookieGenerator.MAX_AGE)
                .domain(this.clientDomain)
                .secure(true)
                .build();
    }
}
