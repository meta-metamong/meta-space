package com.metamong.mt.domain.reservation.exception;

import java.util.NoSuchElementException;

import lombok.Getter;

@Getter
public class ReservationNotFoundException extends NoSuchElementException {
    private final Long rvtId;

    public ReservationNotFoundException(Long rvtId, String message, Throwable e) {
        super(message, e);
        this.rvtId = rvtId;
    }
    
    public ReservationNotFoundException(Long rvtId, String message) {
        super(message);
        this.rvtId = rvtId;
    }
    
    public ReservationNotFoundException(Long rvtId, Throwable e) {
        super(e);
        this.rvtId = rvtId;
    }
    
    public ReservationNotFoundException(Long rvtId) {
        super();
        this.rvtId = rvtId;
    }
}
