package com.metamong.mt.domain.notification.exception;

public class FailedNotificationTransmissionException extends RuntimeException {

    public FailedNotificationTransmissionException(String msg) {
        super(msg);
    }
    
    public FailedNotificationTransmissionException(Throwable e) {
        super(e);
    }
    
    public FailedNotificationTransmissionException(String msg, Throwable e) {
        super(msg, e);
    }
}
