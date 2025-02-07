package com.metamong.mt.domain.reservation.exception;

public class ReservationDuplicatedException extends RuntimeException {
    
    public ReservationDuplicatedException(String message) {
        super(message);
    }
}
