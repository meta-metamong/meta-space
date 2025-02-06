package com.metamong.mt.domain.member.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.metamong.mt.domain.member.repository.redis.MemberEmailCodeRepository;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.global.mail.MailType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultEmailValidationService implements EmailValidationService {
    private static final char[] MAIL_VALIDATION_CODE_CHARACTERS;
    private static final int MAIL_VALIDATION_CODE_LENGTH = 6;
    
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
    private final MemberEmailCodeRepository memberVolatileCodeRepository;

    @Override
    public void sendEmailValidationCode(String email) {
        final String validationCode = generateRandomValue();
        if (log.isDebugEnabled()) {
            log.debug("Generated validation code: {}", validationCode);
        }
        this.mailAgent.send(MailType.MAIL_VALIDATION, "[Metaspace] 메일 인증 번호", email, validationCode);
        this.memberVolatileCodeRepository.saveEmailValidationCode(email, validationCode);
    }

    @Override
    public boolean isValidCode(String email, String emailValidationCode) {
        String codeFromRepository = this.memberVolatileCodeRepository.findEmailValidationCodeByEmail(email);
        if (emailValidationCode.equals(codeFromRepository)) {
            this.memberVolatileCodeRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }
    
    private String generateRandomValue() {
        Random random = new Random();
        char[] codeCharArray = new char[MAIL_VALIDATION_CODE_LENGTH];
        for (int i = 0; i < MAIL_VALIDATION_CODE_LENGTH; i++) {
            codeCharArray[i] = MAIL_VALIDATION_CODE_CHARACTERS[Math.abs(random.nextInt()) % MAIL_VALIDATION_CODE_CHARACTERS.length];
        }
        return String.valueOf(codeCharArray);
    }
}
