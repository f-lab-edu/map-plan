package com.mapwithplan.mapplan.common.timeutils.infrastructure;

import com.mapwithplan.mapplan.common.timeutils.service.port.LocalDateTimeClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class ClockHolder implements LocalDateTimeClockHolder {

    @Override
    public LocalDateTime clockHold() {

        // 현재 UTC 기준의 시간을 가져오기
        Instant currentUtcTime = Instant.now();
        // UTC 시간을 LocalDateTime으로 변환하기
        return LocalDateTime.ofInstant(currentUtcTime, ZoneOffset.UTC);
    }
}

