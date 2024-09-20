package com.sparta.fmdelivery.exception;

import com.sparta.fmdelivery.apipayload.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    private final BaseCode errorCode;
}
