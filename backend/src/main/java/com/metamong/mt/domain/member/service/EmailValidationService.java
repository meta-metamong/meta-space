package com.metamong.mt.domain.member.service;

public interface EmailValidationService {

    void sendEmailValidationCode(String email);
    
    boolean isValidCode(String emailValidationCode);
}
