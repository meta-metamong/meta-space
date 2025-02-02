package com.metamong.mt.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvalidLoginRequestException extends RuntimeException {
    private final InvalidLoginRequestType invalidLoginRequestType;
    
    public InvalidLoginRequestException(InvalidLoginRequestType invalidLoginRequestType, Throwable e) {
        super(e);
        this.invalidLoginRequestType = invalidLoginRequestType;
    }
}
