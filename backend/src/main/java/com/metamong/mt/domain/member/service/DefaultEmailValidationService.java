package com.metamong.mt.domain.member.service;

import org.springframework.stereotype.Service;

import com.metamong.mt.global.mail.MailAgent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultEmailValidationService implements EmailValidationService {
    private final MailAgent mailAgent;

    @Override
    public void sendEmailValidationCode(String email) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isValidCode(String emailValidationCode) {
        // TODO Auto-generated method stub
        return false;
    }
}
