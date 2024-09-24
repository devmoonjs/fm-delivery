package com.sparta.fmdelivery.domain.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.fmdelivery.domain.notification.entity.Notification;
import com.sparta.fmdelivery.domain.notification.repository.RedisEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final RedisEmitterRepository redisEmitterRepository;

    public void handleMessage(String message) {
        try {
            Notification notification = objectMapper.readValue(message, Notification.class);

            for (Long userId : redisEmitterRepository.getAllEmitter()) {
                SseEmitter emitter = redisEmitterRepository.getEmitter(userId);

                if (emitter != null) {
                    emitter.send(SseEmitter.event().name("notificaiton").data(notification));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
