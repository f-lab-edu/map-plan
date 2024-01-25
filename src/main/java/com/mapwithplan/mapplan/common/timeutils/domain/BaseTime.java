package com.mapwithplan.mapplan.common.timeutils.domain;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;



@Getter
public class BaseTime {


    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public BaseTime(LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
