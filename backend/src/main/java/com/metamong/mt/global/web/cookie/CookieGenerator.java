package com.metamong.mt.global.web.cookie;

import org.springframework.http.ResponseCookie;

public interface CookieGenerator {
    long MAX_AGE = 60 * 60 * 24 * 7;
    String PATH = "/";

    ResponseCookie generateCookie(String name, String value);
}
