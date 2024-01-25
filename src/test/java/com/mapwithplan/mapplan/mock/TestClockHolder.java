package com.mapwithplan.mapplan.mock;


import com.mapwithplan.mapplan.common.timeutils.service.port.LocalDateTimeClockHolder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TestClockHolder implements LocalDateTimeClockHolder {

    private final LocalDateTime localDateTime;

    @Override
    public LocalDateTime clockHold() {
        return localDateTime;
    }
}
