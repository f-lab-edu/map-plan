package com.mapwithplan.mapplan.login.controller;


import com.mapwithplan.mapplan.login.controller.response.LoginResponse;
import com.mapwithplan.mapplan.login.domain.Login;
import com.mapwithplan.mapplan.login.service.LoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid Login login){
        log.info("getEmail = {}, getPassword = {}",login.getEmail(),login.getPassword());
        loginService.login(login);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginService.login(login));
    }
}
