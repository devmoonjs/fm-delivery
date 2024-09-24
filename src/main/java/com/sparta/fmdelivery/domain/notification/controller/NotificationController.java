package com.sparta.fmdelivery.domain.notification.controller;

import com.sparta.fmdelivery.domain.notification.dto.request.NoticeCreateRequest;
import com.sparta.fmdelivery.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/notices")
    public ResponseEntity<String> sendNoticeToAll(@RequestBody NoticeCreateRequest request) {
        notificationService.sendNoticeToAll(request);
        return ResponseEntity.ok("전체 유저에게 메세지 전송 완료.");
    }
}
