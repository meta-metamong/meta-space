package com.metamong.mt.domain.member.repository.redis;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

class MemberVolatileCodeRepositoryTest {
    MemberVolatileCodeRepository volatileCodeRepository;
    LettuceConnectionFactory connectionFactory;

    @BeforeEach
    void beforeEach() {
        this.connectionFactory = new LettuceConnectionFactory(
                "localhost", 6379);
          
        this.connectionFactory.start();
        
        this.volatileCodeRepository = new MemberVolatileCodeRepository(connectionFactory);
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
}
