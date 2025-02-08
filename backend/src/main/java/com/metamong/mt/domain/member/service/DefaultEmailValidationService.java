package com.metamong.mt.domain.member.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.member.exception.InvalidEmailValidationCodeException;
import com.metamong.mt.domain.member.repository.redis.MemberEmailCodeRepository;
import com.metamong.mt.domain.member.repository.redis.MemberVolatileCodeRepository;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.global.mail.MailType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultEmailValidationService implements EmailValidationService {
    private static final char[] MAIL_VALIDATION_CODE_CHARACTERS;
    
    static {
        char[] chars = new char['z' - 'a' + 1 + '9' - '0' + 1];
        
        final int alphabetCount = 'z' - 'a' + 1;
        for (int i = 0; i < alphabetCount; i++) {
            chars[i] = (char)('a' + i);
        }
        
        for (int i = 0; i < 10; i++) {
            chars[i + alphabetCount] = (char)('0' + i);
        }
        
        MAIL_VALIDATION_CODE_CHARACTERS = chars;
    }
    
    private final MailAgent mailAgent;
    private final MemberVolatileCodeRepository memberVolatileCodeRepository;

    @Override
    public String sendEmailValidationCode(String email) {
        final String validationCode = generateRandomValue(6);
        if (log.isDebugEnabled()) {
            log.debug("Generated validation code: {}", validationCode);
        }
        this.mailAgent.send(MailType.MAIL_VALIDATION, "[Metaspace] 메일 인증 번호", email, validationCode);
        this.memberVolatileCodeRepository.saveEmailValidationCode(email, validationCode);
        return validationCode;
    }

    @Override
    public String validateCode(String email, String emailValidationCode) {
        String codeFromRepository = this.memberVolatileCodeRepository.findEmailValidationCodeByEmail(email);
        if (codeFromRepository != null && emailValidationCode.equals(codeFromRepository)) {
            this.memberVolatileCodeRepository.deleteByEmail(email);
            String signUpValidationCode = generateRandomValue(15);
            this.memberVolatileCodeRepository.saveSignUpValidationCode(email, signUpValidationCode);
            return signUpValidationCode;
        }
        throw new InvalidEmailValidationCodeException(email);
    }
    
    private String generateRandomValue(int size) {
        Random random = new Random();
        char[] codeCharArray = new char[size];
        for (int i = 0; i < size; i++) {
            codeCharArray[i] = MAIL_VALIDATION_CODE_CHARACTERS[Math.abs(random.nextInt()) % MAIL_VALIDATION_CODE_CHARACTERS.length];
        }
        return String.valueOf(codeCharArray);
    }

    @Override
    public boolean isValidSignUpValidationCode(String email, String signUpValidationCode) {
        String codeFromRepository = this.memberVolatileCodeRepository.findEmailValidationCodeByEmail(email);

        if (codeFromRepository == null || !codeFromRepository.equals(signUpValidationCode)) {
            return false;
        }
        this.memberVolatileCodeRepository.deleteByEmail(email);
        return true;
    }
}
