package com.metamong.mt.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvalidEmailValidationCodeException extends RuntimeException {
    private final String email;
}
