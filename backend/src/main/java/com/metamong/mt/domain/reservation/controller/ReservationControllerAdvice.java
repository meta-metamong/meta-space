package com.metamong.mt.domain.reservation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metamong.mt.domain.reservation.exception.ReservationDuplicatedException;
import com.metamong.mt.domain.reservation.exception.ReservationNotFoundException;
import com.metamong.mt.domain.reservation.exception.errorcode.ReservationErrorCode;
import com.metamong.mt.global.apispec.ErrorResponse;

@RestControllerAdvice(basePackages = "com.metamong.mt.domain")
public class ReservationControllerAdvice {

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<?> reservationNotFoundException(ReservationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.of(ReservationErrorCode.RVT_NOT_FOUND));
    }
    
    @ExceptionHandler(ReservationDuplicatedException.class)
    public ResponseEntity<?> reservationDuplicatedException(ReservationDuplicatedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(ErrorResponse.of(ReservationErrorCode.DUPLICATED_RESERVATION));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                                .map(error -> error.getDefaultMessage())
                                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(ErrorResponse.of(ReservationErrorCode.INVALID_REQUEST, errors.toString()));
    }
}
