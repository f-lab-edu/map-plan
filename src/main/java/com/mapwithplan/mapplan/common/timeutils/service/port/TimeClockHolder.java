package com.mapwithplan.mapplan.common.timeutils.service.port;

import java.time.LocalDateTime;
import java.util.Date;

public interface TimeClockHolder {

    LocalDateTime clockHold();

    Date dateClockHold();
}
