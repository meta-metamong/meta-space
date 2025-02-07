package com.metamong.mt.domain.member.exception;

public class IllegalSignUpRequestException extends RuntimeException {

    public IllegalSignUpRequestException(String msg) {
        super(msg);
    }
}
