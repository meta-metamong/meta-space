package com.metamong.mt.domain.member.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metamong.mt.domain.member.exception.EmailAlreadyExistException;
import com.metamong.mt.domain.member.exception.InvalidLoginRequestException;
import com.metamong.mt.domain.member.exception.InvalidPasswordResetRequestException;
import com.metamong.mt.domain.member.exception.MemberNotFoundException;
import com.metamong.mt.domain.member.exception.PasswordNotConfirmedException;
import com.metamong.mt.domain.member.exception.errorcode.MemberErrorCode;
import com.metamong.mt.global.apispec.BaseResponse;
import com.metamong.mt.global.apispec.ErrorResponse;

@RestControllerAdvice(basePackages = "com.metamong.mt.domain")
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
    
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> idEmailAleadyExistException(EmailAlreadyExistException e) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(ErrorResponse.of(MemberErrorCode.EMAIL_ALEADY_EXIST));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                                .map(error -> error.getDefaultMessage())
                                .collect(Collectors.toList());
        System.out.println(errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(BaseResponse.of(HttpStatus.BAD_REQUEST, errors.toString()));
    }
}
