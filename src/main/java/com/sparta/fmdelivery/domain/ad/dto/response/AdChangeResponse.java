package com.sparta.fmdelivery.domain.ad.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdChangeResponse {

    private final Long adId;
    private final Long storeId;
    private final String comment;
    private final String status;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
}
