package com.mapwithplan.mapplan.loginlogout.controller;


import com.mapwithplan.mapplan.loginlogout.controller.port.LoginService;
import com.mapwithplan.mapplan.loginlogout.controller.response.LoginResponse;
import com.mapwithplan.mapplan.loginlogout.domain.DeleteRefreshToken;
import com.mapwithplan.mapplan.loginlogout.domain.Login;


import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Builder
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class LoginLogoutController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login login){

        log.info("getEmail = {}, getPassword = {}",login.getEmail(),login.getPassword());
        LoginResponse loginResponse = loginService.login(login);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginResponse);
    }


    @DeleteMapping("/logout")
    public ResponseEntity<DeleteRefreshToken> logout(@RequestBody DeleteRefreshToken deleteRefreshToken){
        log.info("deleteLogout {} ", deleteRefreshToken.getRefreshToken());
        loginService.logout(deleteRefreshToken.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deleteRefreshToken);
    }
}
