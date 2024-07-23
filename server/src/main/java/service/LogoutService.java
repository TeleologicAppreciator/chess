package service;

import dataaccess.AuthDAO;
import model.AuthData;
import request.LogoutRequest;
import result.LogoutResult;

public class LogoutService {
    private AuthDAO myAuthData;

    public LogoutService(AuthDAO theAuthData) {
        myAuthData = theAuthData;
    }

    public LogoutResult logout(LogoutRequest theLogoutRequest) {
        AuthData authDataToCheck = myAuthData.getAuth(theLogoutRequest.myAuthToken());

        myAuthData.deleteAuth(authDataToCheck);

        return new LogoutResult(Integer.valueOf(1));
    }
}
