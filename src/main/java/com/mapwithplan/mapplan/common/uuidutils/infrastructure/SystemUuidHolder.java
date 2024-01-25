package com.mapwithplan.mapplan.common.uuidutils.infrastructure;


import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
