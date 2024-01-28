package com.mapwithplan.mapplan.login.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Login {

    private final String email;
    private final String password;


    public Login(@JsonProperty("email") String email,
                 @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }
}
