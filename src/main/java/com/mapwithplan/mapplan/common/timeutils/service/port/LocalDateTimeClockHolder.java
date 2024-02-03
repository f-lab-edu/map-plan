package com.mapwithplan.mapplan.common.timeutils.service.port;

import java.time.LocalDateTime;


/**
 * 시간을 기록할때 사용하는 인터페이스입니다.
 */
public interface LocalDateTimeClockHolder {

    LocalDateTime clockHold();
}
