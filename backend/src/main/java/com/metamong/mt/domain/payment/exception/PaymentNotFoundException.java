package com.metamong.mt.domain.payment.exception;

import java.util.NoSuchElementException;

public class PaymentNotFoundException extends NoSuchElementException {

    public PaymentNotFoundException(String message, Throwable e) {
        super(message, e);
    }
    
    public PaymentNotFoundException(String message) {
        super(message);
    }
    
    public PaymentNotFoundException(Throwable e) {
        super(e);
    }
    
    public PaymentNotFoundException() {
        super();
    }
}