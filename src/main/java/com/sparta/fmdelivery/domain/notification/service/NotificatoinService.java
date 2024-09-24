package com.sparta.fmdelivery.domain.notification.service;

import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificatoinService {

    private final static Long DEFAULT_TIMEOUT = 60_000L;
    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(AuthUser authUser) {

        Long userId = authUser.getId();

        SseEmitter emitter = createEmitter(userId);

        sendToClient(userId, "Event Created. [userId = " + userId + "]");
        log.info("finish");

        return emitter;
    }

    private void sendToClient(Long userId, Object data) {

        SseEmitter emitter = emitterRepository.getEmitter(userId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(userId)).name("sse").data(data));
                emitter.send("success");
            } catch (IOException exception) {
                emitterRepository.removeEmitter(userId);
                emitter.completeWithError(exception);
            }
        }
    }

    private SseEmitter createEmitter(Long userId) {

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.addEmitter(userId, emitter);
        log.info(emitter.toString());

        // 완료 OR 타임아웃일 경우 Emitter 삭제
        emitter.onCompletion(() -> emitterRepository.removeEmitter(userId));
        emitter.onTimeout(() -> emitterRepository.removeEmitter(userId));

        return emitter;
    }
}
