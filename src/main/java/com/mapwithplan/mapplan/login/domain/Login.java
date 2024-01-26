package com.mapwithplan.mapplan.login.domain;

import lombok.Getter;

@Getter
public class Login {

    private final String email;
    private final String password;


    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
