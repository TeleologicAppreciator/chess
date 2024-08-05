package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import server.request.LogoutRequest;
import server.result.Result;

public class LogoutService extends AuthService {
    public LogoutService(AuthDAO theAuthData) {
        super(theAuthData);
    }

    public Result logout(LogoutRequest theLogoutRequest) {
        if (isNotAuthorized(theLogoutRequest.authToken())) {
            return new Result("Error: unauthorized");
        }

        AuthData currentSessionToLogout = null;

        try {
            currentSessionToLogout = this.getAuthData().getAuth(theLogoutRequest.authToken());
        } catch (DataAccessException e) {
            //we have already verified that the user is valid
        }

        try {
            this.getAuthData().deleteAuth(currentSessionToLogout);
        } catch (Exception e) {
            return new Result("Unable to read data");
        }

        return new Result();
    }
}
