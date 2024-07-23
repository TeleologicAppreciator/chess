package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import request.LogoutRequest;
import result.LogoutResult;

public class LogoutService {
    private AuthDAO myAuthData;

    public LogoutService(AuthDAO theAuthData) {
        myAuthData = theAuthData;
    }

    public LogoutResult logout(LogoutRequest theLogoutRequest) {
        AuthData authDataToCheck = null;

        try {
            authDataToCheck = myAuthData.getAuth(theLogoutRequest.myAuthToken());
        } catch(DataAccessException e) {
            return new LogoutResult("Error: unauthorized");
        }

        myAuthData.deleteAuth(authDataToCheck);

        return new LogoutResult();
    }
}
