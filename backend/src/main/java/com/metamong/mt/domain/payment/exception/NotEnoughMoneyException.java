package com.metamong.mt.domain.payment.exception;

import java.util.NoSuchElementException;

public class NotEnoughMoneyException extends NoSuchElementException {

    public NotEnoughMoneyException(String message, Throwable e) {
        super(message, e);
    }
    
    public NotEnoughMoneyException(String message) {
        super(message);
    }
    
    public NotEnoughMoneyException(Throwable e) {
        super(e);
    }
    
    public NotEnoughMoneyException() {
        super();
    }
}