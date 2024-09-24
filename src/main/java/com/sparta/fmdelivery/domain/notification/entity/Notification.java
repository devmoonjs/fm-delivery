package com.sparta.fmdelivery.domain.notification.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Notification{

    private String message;

    public Notification(String message) {
        this.message = message;
    }
}
