package com.metamong.mt.domain.reservation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metamong.mt.domain.reservation.exception.ReservationNotFoundException;
import com.metamong.mt.global.apispec.BaseResponse;

@RestControllerAdvice(basePackages = "com.metamong.mt.domain")
public class ReservationControllerAdvice {

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<?> memberNotFoundException(ReservationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(BaseResponse.of(HttpStatus.NOT_FOUND));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                                .map(error -> error.getDefaultMessage())
                                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(BaseResponse.of(HttpStatus.BAD_REQUEST, errors.toString()));
    }
}
