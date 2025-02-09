package com.metamong.mt.domain.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metamong.mt.domain.payment.exception.AccountNotFoundException;
import com.metamong.mt.domain.payment.exception.NotEnoughMoneyException;
import com.metamong.mt.domain.payment.exception.PaymentNotFoundException;
import com.metamong.mt.domain.payment.exception.errorcode.PaymentErrorCode;
import com.metamong.mt.global.apispec.ErrorResponse;

@RestControllerAdvice(basePackages = "com.metamong.mt.domain")
public class PaymentControllerAdvice {
    
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<?> paymentNotFoundException(PaymentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(PaymentErrorCode.PAY_NOT_FOUND));
    }
    
    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<?> notEnoughMoneyException(NotEnoughMoneyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(PaymentErrorCode.NOT_ENOUGH_MONEY));
    }
    
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(PaymentErrorCode.ACCOUNT_NOT_FOUND));
    }
}
