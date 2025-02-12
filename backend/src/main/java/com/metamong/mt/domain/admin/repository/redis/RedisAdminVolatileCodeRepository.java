package com.metamong.mt.domain.admin.repository.redis;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metamong.mt.domain.admin.dto.response.WeekReservationDto;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Repository
public class RedisAdminVolatileCodeRepository {

    private final RedisTemplate<String, String> stringRedisTemplate;
    
    public RedisAdminVolatileCodeRepository(RedisConnectionFactory connectionFactory) {
        this.stringRedisTemplate = new RedisTemplate<>();
        this.stringRedisTemplate.setConnectionFactory(connectionFactory);
        this.stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        this.stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        this.stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        this.stringRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        this.stringRedisTemplate.afterPropertiesSet();
    }
    
    public void set(String key, List<WeekReservationDto> value, int timeout, TimeUnit timeUnit) {
        try {
            String jsonValue = new ObjectMapper().writeValueAsString(value);
            stringRedisTemplate.opsForValue().set(key, jsonValue, timeout, timeUnit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<WeekReservationDto> get(String key) {
        String jsonValue = (String) stringRedisTemplate.opsForValue().get(key);
        try {
            return new ObjectMapper().readValue(jsonValue, new com.fasterxml.jackson.core.type.TypeReference<List<WeekReservationDto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

	
	

