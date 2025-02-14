package com.metamong.mt.domain.notification.repository.jpa;

import java.util.Optional;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import com.metamong.mt.domain.notification.model.Notification;

@Repository
public class CachingNotificationRepository implements NotificationRepository {
    private static final String PREFIX = "NOTIFICATION_COUNT_";
    
    private final JpaNotificationRepository jpaNotificationRepository;
    private final RedisTemplate<String, Integer> redisTemplate;
    
    public CachingNotificationRepository(
            JpaNotificationRepository jpaNotificationRepository,
            RedisConnectionFactory redisConnectionFactory
    ) {
        this.jpaNotificationRepository = jpaNotificationRepository;
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setConnectionFactory(redisConnectionFactory);
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Integer.class));
        this.redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        
        this.redisTemplate.afterPropertiesSet();
    }
    
    
    @Override
    public int countNotReadNotificationsByReceiverId(Long receiverId) {
        Integer count = this.redisTemplate.opsForValue().get(PREFIX + receiverId);
        if (count == null) {
            Integer countResult = this.jpaNotificationRepository.countNotReadNotificationsByReceiverId(receiverId);
            this.redisTemplate.opsForValue().set(PREFIX + receiverId, countResult);
            count = countResult;
        }
        return count;
    }

    @Override
    public Notification save(Notification notification) {
        Long receiverId = notification.getReceiverId();
        Integer count = this.redisTemplate.opsForValue().get(PREFIX + receiverId);
        if (count != null && notification.getIsRead().equals('N')) {
            this.redisTemplate.opsForValue().set(PREFIX + receiverId, count + 1);
        }
        return this.jpaNotificationRepository.save(notification);
    }

    @Override
    public void deleteById(Long notiId) {
        Optional<Notification> notification = this.jpaNotificationRepository.findById(notiId);
        notification.ifPresent((noti) -> {
            Long receiverId = noti.getReceiverId();
            Integer count = this.redisTemplate.opsForValue().get(PREFIX + receiverId);
            if (count != null && count > 0 && noti.getIsRead().equals('N')) {
                this.redisTemplate.opsForValue().set(PREFIX + receiverId, count - 1);
            }
            this.jpaNotificationRepository.deleteById(notiId);
        });
    }
    
    @Override
    public void readByNotificationId(Long notificationId) {
        Optional<Notification> notification = this.jpaNotificationRepository.findById(notificationId);
        notification.ifPresent((noti) -> {
            Long receiverId = noti.getReceiverId();
            Integer count = this.redisTemplate.opsForValue().get(PREFIX + receiverId);
            if (count != null && count > 0 && noti.getIsRead().equals('N')) {
                this.redisTemplate.opsForValue().set(PREFIX + receiverId, count - 1);
            }
            noti.setIsRead('Y');
        });
    }
}
