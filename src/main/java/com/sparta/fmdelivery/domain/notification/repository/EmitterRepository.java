package com.sparta.fmdelivery.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public SseEmitter getEmitter(Long userId) {
        return emitters.get(userId);
    }

    public void removeEmitter(Long userId) {
        emitters.remove(userId);
    }
}
