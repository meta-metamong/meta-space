package com.metamong.mt.global.mail.exception;

public class UnsupportedMailTypeException extends RuntimeException {

    public UnsupportedMailTypeException(String message) {
        super(message);
    }
    
    public UnsupportedMailTypeException(Throwable e) {
        super(e);
    }
    
    public UnsupportedMailTypeException(String message, Throwable e) {
        super(message, e);
    }
}
