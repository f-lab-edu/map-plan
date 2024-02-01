package com.mapwithplan.mapplan.loginlogout.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DeleteRefreshToken {

    private final String refreshToken;

    public DeleteRefreshToken(@JsonProperty("refreshToken")
                              String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
