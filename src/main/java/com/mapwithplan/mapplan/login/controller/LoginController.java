package com.mapwithplan.mapplan.login.controller;


import com.mapwithplan.mapplan.login.domain.Login;
import com.mapwithplan.mapplan.login.service.LoginService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Login login){

        return ResponseEntity.ok().build();
    }
}
