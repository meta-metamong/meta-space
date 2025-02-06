package com.metamong.mt.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.metamong.mt.domain.member.repository.redis.MemberVolatileCodeRepository;
import com.metamong.mt.global.mail.MailAgent;
import com.metamong.mt.global.mail.MailType;
import com.metamong.mt.global.mail.exception.MailTransmissionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class DefaultEmailValidationServiceTest {
    DefaultEmailValidationService defaultEmailValidationService;
    MailAgentMocking mailAgent;
    MemberVolatileCodeRepository memberVolatileCodeRepository;
    
    @RequiredArgsConstructor
    static class MailAgentMocking implements MailAgent {
        private static final Map<String, String> mailCodeMap = new HashMap<>();
        private final MemberVolatileCodeRepository memberVolatileCodeRepository;

        @Override
        public void send(MailType mailType, String subject, String receiverEmail, Object... params)
                throws MailTransmissionException {
            if (mailType != MailType.MAIL_VALIDATION) {
                throw new UnsupportedOperationException("지원하지 않은 MailType=" + mailType);
            }
            
            mailCodeMap.put(receiverEmail, (String) params[0]);
            this.memberVolatileCodeRepository.saveEmailValidationCode(receiverEmail, (String) params[0]);
        }
        
        public Optional<String> getMailMessageMock(String email) {
            return Optional.of(mailCodeMap.get(email));
        }
    }
    
    static class MemberVolatileCodeRepositoryMock implements MemberVolatileCodeRepository {
        private static final Map<String, String> store = new HashMap<>();

        @Override
        public void saveEmailValidationCode(String email, String code) {
            store.put(email, code);
        }

        @Override
        public String findEmailValidationCodeByEmail(String email) {
            return store.get(email);
        }

        @Override
        public boolean deleteByEmail(String email) {
            boolean result = store.containsKey(email);
            store.remove(email);
            return result;
        }
    }
    
    @BeforeEach
    void beforeEach() {
        this.memberVolatileCodeRepository = new MemberVolatileCodeRepositoryMock();
        this.mailAgent = new MailAgentMocking(memberVolatileCodeRepository);
        this.defaultEmailValidationService = new DefaultEmailValidationService(this.mailAgent, this.memberVolatileCodeRepository);
    }

    @Test
    @DisplayName("Send validation code and validate code")
    void sendValidationCodeAndValidate() {
        final String email = "test@gmail.com";
        this.defaultEmailValidationService.sendEmailValidationCode(email);
        String validationCode = this.mailAgent.getMailMessageMock(email).orElseThrow(() -> new RuntimeException());
        
        log.info("validationCode={}", validationCode);
        log.info("inRepository={}", this.memberVolatileCodeRepository.findEmailValidationCodeByEmail(email));
        
        assertThat(this.defaultEmailValidationService.isValidCode(email, validationCode)).isTrue();
        assertThat(this.defaultEmailValidationService.isValidCode(email, validationCode)).isFalse();
    }
}
