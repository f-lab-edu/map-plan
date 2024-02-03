package com.mapwithplan.mapplan.common.uuidutils.infrastructure;


import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * UUID 를 생성하는 클래스입니다.
 */
@Component
public class SystemUuidHolder implements UuidHolder {

    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
