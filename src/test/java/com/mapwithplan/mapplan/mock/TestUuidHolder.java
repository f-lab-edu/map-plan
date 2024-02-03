package com.mapwithplan.mapplan.mock;


import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {

    private final String uuid;
    @Override
    public String random() {
        return uuid;
    }
}
