package com.metamong.mt.domain.report.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metamong.mt.domain.report.exception.ReportNotFoundException;
import com.metamong.mt.domain.report.exception.errorcode.ReportErrorCode;
import com.metamong.mt.global.apispec.ErrorResponse;

@RestControllerAdvice(basePackages = "com.metamong.mt.domain")
public class ReportControllerAdvice {
    
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<?> paymentNotFoundException(ReportNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ReportErrorCode.REPORT_NOT_FOUND));
    }
}
