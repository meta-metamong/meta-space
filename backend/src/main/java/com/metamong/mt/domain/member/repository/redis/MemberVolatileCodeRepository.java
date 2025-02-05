package com.metamong.mt.domain.member.repository.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class MemberVolatileCodeRepository {
    private static final String EMAIL_VALIDATION_CODE_PREFIX = "email_validation_code";
    
    private final RedisTemplate<String, String> stringRedisTemplate;
    
    public MemberVolatileCodeRepository(RedisConnectionFactory connectionFactory) {
        this.stringRedisTemplate = new RedisTemplate<>();
        this.stringRedisTemplate.setConnectionFactory(connectionFactory);
        this.stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        this.stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        this.stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        this.stringRedisTemplate.afterPropertiesSet();
    }
    
    public void saveEmailValidationCode(String email, String code) {
        this.stringRedisTemplate.opsForValue()
                .set(EMAIL_VALIDATION_CODE_PREFIX + email, code);
    }
    
    public String findEmailValidationCodeByEmail(String email) {
        return this.stringRedisTemplate.opsForValue().get(EMAIL_VALIDATION_CODE_PREFIX + email);
    }
}
