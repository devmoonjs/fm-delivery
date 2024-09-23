package com.sparta.fmdelivery.domain.notification.controller;

import com.sparta.fmdelivery.domain.common.annotation.Auth;
import com.sparta.fmdelivery.domain.common.dto.AuthUser;
import com.sparta.fmdelivery.domain.notification.service.NotificatoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse/v1")
public class NotificationController {

    private final NotificatoinService notificatoinService;

    @GetMapping(value = "/subscribe", produces = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@Auth AuthUser authUser) {
        return notificatoinService.subscribe(authUser);
    }
}
