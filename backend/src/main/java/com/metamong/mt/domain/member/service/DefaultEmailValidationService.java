package com.metamong.mt.domain.member.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metamong.mt.domain.member.dto.request.FindPasswordRequestDto;
import com.metamong.mt.domain.member.exception.InvalidEmailValidationCodeException;
import com.metamong.mt.domain.member.model.Member;
import com.metamong.mt.domain.member.repository.jpa.MemberRepository;
import com.metamong.mt.domain.member.repository.redis.MemberVolatileCodeRepository;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.global.mail.MailType;

import lombok.extern.slf4j.Slf4j;

@Service
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
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final String clientOrigin;

    public DefaultEmailValidationService(MailAgent mailAgent,
            MemberVolatileCodeRepository memberVolatileCodeRepository,
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            @Value("${client.origin}") String clientOrigin) {
        this.mailAgent = mailAgent;
        this.memberVolatileCodeRepository = memberVolatileCodeRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientOrigin = clientOrigin;
    }

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

    @Override
    public void sendPasswordValidationCode(String email) {
        String code = generateRandomValue(15);
        String url = this.clientOrigin + "/find-pw-reset?validation-code=" + code + "&email=" + email;
        this.mailAgent.send(MailType.PASSWORD_RESET_LINK, "패스워드 재설정 링크", email, url);
        this.memberVolatileCodeRepository.savePasswordValidationCode(email, code);
    }

    @Override
    @Transactional
    public boolean resetPassword(FindPasswordRequestDto dto) {
        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            return false;
        }
        
        String codeFromStorage = this.memberVolatileCodeRepository.findPasswordValidationCodeByEmail(dto.getEmail());
        log.debug("codeFromStorage={}", codeFromStorage);
        log.debug("email={}", dto.getEmail());
        if (codeFromStorage == null || !codeFromStorage.equals(dto.getValidationCode())) {
            return false;
        }
        
        Optional<Member> memberOp = this.memberRepository.findByEmail(dto.getEmail());
        if (memberOp.isEmpty()) {
            return false;
        }
        
        this.memberVolatileCodeRepository.deleteByEmail(dto.getEmail());
        
        Member member = memberOp.get();
        
        member.changePassword(this.passwordEncoder.encode(dto.getNewPassword()));
        return true;
    }
}
