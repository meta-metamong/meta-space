package com.metamong.mt.domain.member.controller;

import org.springframework.http.HttpStatus;import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.exception.InvalidPasswordResetRequestException;
import com.metamong.mt.domain.member.exception.MemberNotFoundException;
import com.metamong.mt.domain.member.exception.PasswordNotConfirmedException;
import com.metamong.mt.domain.member.exception.errorcode.MemberErrorCode;
import com.metamong.mt.global.apispec.ErrorResponse;

@RestControllerAdvice(basePackages = "com.metamong.mt.member")
public class MemberControllerAdvice {

    @ExceptionHandler(InvalidLoginRequestException.class)
    public ResponseEntity<ErrorResponse> invalidLoginRequestException(InvalidLoginRequestException e) {
        return new ResponseEntity<>(ErrorResponse.of(MemberErrorCode.INVALID_LOGIN_REQUEST), HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> memberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(ErrorResponse.of(MemberErrorCode.MEMBER_NOT_FOUND, e.getUserId()));
    }
    
    @ExceptionHandler(PasswordNotConfirmedException.class)
    public ResponseEntity<ErrorResponse> passwordNotConfirmedException(PasswordNotConfirmedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(ErrorResponse.of(MemberErrorCode.PASSWORD_NOT_CONFIRMED));
    }
    
    @ExceptionHandler(InvalidPasswordResetRequestException.class)
    public ResponseEntity<ErrorResponse> invalidPasswordResetRequestException(InvalidPasswordResetRequestException e) {
        return new ResponseEntity<>(ErrorResponse.of(MemberErrorCode.INVALID_PASSWORD_RESET_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
