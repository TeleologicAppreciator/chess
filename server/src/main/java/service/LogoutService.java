package service;

import dataaccess.AuthDAO;
import result.*;
import model.AuthData;
import request.LogoutRequest;

public class LogoutService extends AuthService {
    public LogoutService(AuthDAO theAuthData) {
        super(theAuthData);
    }

    public Result logout(LogoutRequest theLogoutRequest) {
        AuthData authDataToVerify = userAuthorizedVerification(theLogoutRequest.myAuthToken());

        Result authorizationResult = unauthorized(authDataToVerify);

        if (authorizationResult != null) {
            return authorizationResult;
        }

        this.getAuthData().deleteAuth(authDataToVerify);

        return new Result();
    }
}
