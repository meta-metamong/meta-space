package com.metamong.mt.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvalidLoginRequestException extends RuntimeException {
    private final InvalidLoginRequestType invalidLoginRequestType;
}
