package com.mapwithplan.mapplan.loginlogout.controller.port;

import com.mapwithplan.mapplan.loginlogout.controller.response.LoginResponse;
import com.mapwithplan.mapplan.loginlogout.domain.Login;

public interface LoginService {

    LoginResponse login(Login login);

    void logout(String memberRefreshTokenUuid);
}
