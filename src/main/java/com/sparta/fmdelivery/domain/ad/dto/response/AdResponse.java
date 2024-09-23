package com.sparta.fmdelivery.domain.ad.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AdResponse {

    private final Long adId;  // 광고 ID
    private final Long storeId;  // 가게 ID
    private final String storeName;  // 가게 이름
    private final String comment;  // 광고 코멘트
    private final String status;  // 광고 상태
    private final LocalDateTime startDate;  // 광고 시작 시간
    private final LocalDateTime endDate;  // 광고 종료 시간
}
