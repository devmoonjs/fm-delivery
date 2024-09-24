package com.sparta.fmdelivery.domain.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.fmdelivery.domain.notification.dto.request.NoticeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(NoticeCreateRequest request) {

        try {
            String message = objectMapper.writeValueAsString(request); // 직렬화
            redisTemplate.convertAndSend("notification-channel", message); // 발행

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
