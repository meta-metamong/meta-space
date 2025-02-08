package com.metamong.mt.global.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String redisHost;
    
    @Value("${redis.port}")
    private int redisPort;
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration config = LettuceClientConfiguration.builder()
                // .useSsl().and()
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build();
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(this.redisHost, this.redisPort),
                config
        );
    }
}
