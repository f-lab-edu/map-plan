package com.mapwithplan.mapplan.loginlogout.controller.port;

import com.mapwithplan.mapplan.loginlogout.controller.response.LoginResponse;
import com.mapwithplan.mapplan.loginlogout.domain.Login;

/**
 * LoginLogoutController 에서 사용할 인터페이스 입니다.
 */
public interface LoginService {


    LoginResponse login(Login login);

    void logout(String memberRefreshTokenUuid);
}
