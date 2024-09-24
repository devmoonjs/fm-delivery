package com.sparta.fmdelivery.domain.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class RedisEmitterRepository {

    private static final String EMITTER_KEY_PRE = "emitter:";
    private final RedisTemplate<String, Object> redisTemplate;

    public void addEmitter(Long userId, SseEmitter emitter) {

        String redisKey = EMITTER_KEY_PRE + userId;

        redisTemplate.opsForValue().set(redisKey, userId, 60, TimeUnit.SECONDS);
    }

    public Set<Long> getAllEmitter() {

        Set<String> keys = redisTemplate.keys(EMITTER_KEY_PRE + "*");

        return keys.stream()
                .map(key -> key.replace(EMITTER_KEY_PRE, ""))
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

    public void removeEmitter(Long userId) {
        String redisKey = EMITTER_KEY_PRE + userId;
        redisTemplate.delete(redisKey);
    }

    public SseEmitter getEmitter(Long userId) {
        String redisKey = EMITTER_KEY_PRE + userId;
        if (redisTemplate.hasKey(redisKey)) {
            return new SseEmitter(60_000L);
        }
        return null;
    }
}
