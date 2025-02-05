package com.metamong.mt.domain.member.repository.redis;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

class RedisMemberVolatileCodeRepositoryTest {
    RedisMemberVolatileCodeRepository volatileCodeRepository;
    LettuceConnectionFactory connectionFactory;

    @BeforeEach
    void beforeEach() {
        this.connectionFactory = new LettuceConnectionFactory(
                "localhost", 6379);
          
        this.connectionFactory.start();
        
        this.volatileCodeRepository = new RedisMemberVolatileCodeRepository(connectionFactory);
    }
    
    @AfterEach
    void afterEach() {
        this.connectionFactory.getConnection().serverCommands().flushAll();
    }

    @Test
    @DisplayName("Save and find success")
    void saveEmailValidationCode_success() {
        final String testEmail = "test@gmail.com";
        final String testCode = "rq80gayf31";
        this.volatileCodeRepository.saveEmailValidationCode(testEmail, testCode);

        String result = this.volatileCodeRepository.findEmailValidationCodeByEmail(testEmail);

        assertThat(result).isEqualTo(testCode);
    }
    
    @Test
    @DisplayName("Save and failed to find")
    void saveEmailValidationCode_failedToFind() {
        final String testEmail = "test@gmail.com";
        final String testCode = "rq80gayf31";
        this.volatileCodeRepository.saveEmailValidationCode(testEmail, testCode);
        
        String result = this.volatileCodeRepository.findEmailValidationCodeByEmail(testEmail + "aa");
        
        assertThat(result).isNull();
    }
    
    @Test
    @DisplayName("delete success")
    void deleteKey_success() {
        final String testEmail = "test@gmail.com";
        final String testCode = "rq80gayf31";
        this.volatileCodeRepository.saveEmailValidationCode(testEmail, testCode);
        
        boolean result = this.volatileCodeRepository.deleteByEmail(testEmail);
        
        assertThat(result).isTrue();
        assertThat(this.volatileCodeRepository.findEmailValidationCodeByEmail(testEmail)).isNull();
    }
    
    @Test
    @DisplayName("Failed to delete since not exact key")
    void deleteKey_failToDelete() {
        final String testEmail = "test@gmail.com";
        final String testCode = "rq80gayf31";
        this.volatileCodeRepository.saveEmailValidationCode(testEmail, testCode);
        
        boolean result = this.volatileCodeRepository.deleteByEmail(testEmail + "aa");
        
        assertThat(result).isFalse();
        assertThat(this.volatileCodeRepository.findEmailValidationCodeByEmail(testEmail)).isNotNull();
    }
}
