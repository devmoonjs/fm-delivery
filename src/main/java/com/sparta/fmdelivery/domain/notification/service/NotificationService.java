package com.sparta.fmdelivery.domain.notification.service;

import com.sparta.fmdelivery.domain.notification.dto.request.NoticeCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final RedisPublisher redisPublisher;

    public void sendNoticeToAll(NoticeCreateRequest request) {
        redisPublisher.publish(request);
    }
}
