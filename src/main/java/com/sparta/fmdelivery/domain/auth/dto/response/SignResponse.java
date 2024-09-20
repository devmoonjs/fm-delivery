package com.sparta.fmdelivery.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignResponse {

    private final String bearerToken;

    public SignResponse(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
