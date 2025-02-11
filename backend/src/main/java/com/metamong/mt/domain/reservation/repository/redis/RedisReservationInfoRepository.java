package com.metamong.mt.domain.reservation.repository.redis;

import java.util.Map;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class RedisReservationInfoRepository implements ReservationInfoRepository {
    private static final String RESERVATION_KEY = "reservations";
    
    private final RedisTemplate<String, Map<String, Object>> redisTemplate;
    
    public RedisReservationInfoRepository(RedisConnectionFactory connectionFactory) {
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setConnectionFactory(connectionFactory);

        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        this.redisTemplate.setValueSerializer(serializer);

        this.redisTemplate.afterPropertiesSet();
    }

    @Override
    public void saveReservationInfo(Map<String, Object> rvtInfoList) {
        this.redisTemplate.opsForValue().set(RESERVATION_KEY, rvtInfoList);
    }

    @Override
    public Map<String, Object> findReservationInfo() {
        return (Map<String, Object>) this.redisTemplate.opsForValue().get(RESERVATION_KEY);
    }

}
