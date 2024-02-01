package com.mapwithplan.mapplan.common.timeutils.infrastructure;

import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class ClockHolder implements TimeClockHolder {

    @Override
    public LocalDateTime clockHold() {

        // 현재 UTC 기준의 시간을 가져오기
        Instant currentUtcTime = Instant.now();
        // UTC 시간을 LocalDateTime으로 변환하기
        return LocalDateTime.ofInstant(currentUtcTime, ZoneOffset.UTC);
    }

    @Override
    public Date dateClockHold() {
        return new Date(System.currentTimeMillis());
    }

}

