package com.sparta.fmdelivery.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories  // Redis Repository 활성화
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // Redis 연결을 위한 RedisConnectionFactory 생성 (Redis 클라이언트로 Lettuce 사용)
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisHost, redisPort);  // .properties 파일에서 설정한 host, port 가져와 연동
    }

    // Redis에 데이터 저장, 검색을 위한 RedisTemplate 설정
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // StringRedisSerializer로 직렬화 방법 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // 문자열 key 저장
        redisTemplate.setValueSerializer(new StringRedisSerializer());  // 문자열 value 저장

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
