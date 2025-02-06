package com.metamong.mt.domain.member.repository.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

/**
 * Redis를 사용한 휘발성 데이터를 저장하는 레포지토리
 */
@Repository
public class RedisMemberVolatileCodeRepository implements MemberVolatileCodeRepository {
    private static final String EMAIL_VALIDATION_CODE_PREFIX = "email_validation_code";
    
    private final RedisTemplate<String, String> stringRedisTemplate;
    
    public RedisMemberVolatileCodeRepository(RedisConnectionFactory connectionFactory) {
        this.stringRedisTemplate = new RedisTemplate<>();
        this.stringRedisTemplate.setConnectionFactory(connectionFactory);
        this.stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        this.stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        this.stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        this.stringRedisTemplate.afterPropertiesSet();
    }
    
    @Override
    public void saveEmailValidationCode(String email, String code) {
        this.stringRedisTemplate.opsForValue()
                .set(EMAIL_VALIDATION_CODE_PREFIX + email, code);
    }
    
    @Override
    public String findEmailValidationCodeByEmail(String email) {
        return this.stringRedisTemplate.opsForValue().get(EMAIL_VALIDATION_CODE_PREFIX + email);
    }
    
    @Override
    public boolean deleteByEmail(String email) {
        return this.stringRedisTemplate.delete(EMAIL_VALIDATION_CODE_PREFIX + email);
    }
}
