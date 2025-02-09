package com.metamong.mt.domain.payment.exception;

import java.util.NoSuchElementException;

public class AccountNotFoundException extends NoSuchElementException {

    public AccountNotFoundException(String message, Throwable e) {
        super(message, e);
    }
    
    public AccountNotFoundException(String message) {
        super(message);
    }
    
    public AccountNotFoundException(Throwable e) {
        super(e);
    }
    
    public AccountNotFoundException() {
        super();
    }
}