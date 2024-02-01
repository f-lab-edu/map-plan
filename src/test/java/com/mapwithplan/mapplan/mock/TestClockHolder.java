package com.mapwithplan.mapplan.mock;


import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
public class TestClockHolder implements TimeClockHolder {

    private final Long longValue ;

    @Override
    public LocalDateTime clockHold() {
        // Long을 Instant으로 변환
        Instant instant = Instant.ofEpochMilli(longValue);

        // Instant을 LocalDateTime으로 변환
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Override
    public Date dateClockHold() {
        return new Date(longValue);
    }
}
