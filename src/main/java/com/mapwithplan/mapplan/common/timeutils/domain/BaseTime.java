package com.mapwithplan.mapplan.common.timeutils.domain;


import lombok.Getter;

import java.time.LocalDateTime;


/**
 * 도메인에 대한 생성 그리고 수정 시간을 기록할때 사용하는 공용 도메인입니다.
 * 도메인에만 사용을 해야합니다.
 */

@Getter
public class BaseTime {


    protected LocalDateTime createdAt;

    protected LocalDateTime modifiedAt;

    public BaseTime(LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
