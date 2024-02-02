package com.mapwithplan.mapplan.loginlogout.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * refreshToken 을 삭제하기 위한 토큰 정보를 담는 Dto 입니다.
 */
@Getter
public class DeleteRefreshToken {

    private final String refreshToken;

    public DeleteRefreshToken(@JsonProperty("refreshToken")
                              String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
