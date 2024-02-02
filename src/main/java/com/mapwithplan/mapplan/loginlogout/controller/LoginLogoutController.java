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

/**
 * 회원의 로그인 로그아웃을 다루는 controller 입니다.
 */
@Builder
@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class LoginLogoutController {

    private final LoginService loginService;

    /**
     * 로그인 시도시 loginService 를 호출하는 메서드 입니다.
     * @param login email password 에 대한 정보를 담고 있습니다.
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login login){

        log.info("getEmail = {}, getPassword = {}",login.getEmail(),login.getPassword());
        LoginResponse loginResponse = loginService.login(login);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginResponse);
    }

    /**
     * 로그아웃 요청시 RefreshToken 을 DB 에서 삭제합니다. -> 추가로 프로트 엔드 작업에 클라이언트의
     * 로컬 스토리지에 있는 AccessToken 삭제를 요청합니다.
     * @param deleteRefreshToken refreshToken 에 대한 JWT 를 담고 있습니다.
     * @return
     */
    @DeleteMapping("/logout")
    public ResponseEntity<DeleteRefreshToken> logout(@RequestBody DeleteRefreshToken deleteRefreshToken){
        log.info("deleteLogout {} ", deleteRefreshToken.getRefreshToken());
        loginService.logout(deleteRefreshToken.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deleteRefreshToken);
    }
}
