package com.metamong.mt.global.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisConnectionTest {
    
    @Test
    @DisplayName("OpsForValue simple string get/set")
    void testOpsForValue_string() {
        // Given
        LettuceClientConfiguration config = LettuceClientConfiguration.builder()
//                .useSsl().and()
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build();
        
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(
                "localhost", 6379);
        
        connectionFactory.start();
        
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        
        // When
        final String key = "key1";
        final String value = "hello!";
        valueOps.set(key, value);
        
        String result = valueOps.get(key);
        log.info("result={}", result);

        // Then
        assertThat(result).isEqualTo(value);
    }
}
