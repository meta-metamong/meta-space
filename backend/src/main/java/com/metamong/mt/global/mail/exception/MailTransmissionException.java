package com.metamong.mt.global.mail.exception;

public class MailTransmissionException extends RuntimeException {

    public MailTransmissionException(String message) {
        super(message);
    }
    
    public MailTransmissionException(String message, Throwable e) {
        super(message, e);
    }
    
    public MailTransmissionException(Throwable e) {
        super(e);
    }
}
