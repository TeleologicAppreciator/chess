package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import result.Result;

public class AuthService {
    private AuthDAO myAuthData;

    public AuthService(AuthDAO theAuthData) {
        myAuthData = theAuthData;
    }

    public AuthData userAuthorizedVerification(String theAuthToken) {
        AuthData userExistsIfAuthorized;

        try {
            userExistsIfAuthorized = myAuthData.getAuth(theAuthToken);
        } catch (DataAccessException e) {
            return null;
        }

        return userExistsIfAuthorized;
    }

    public Result unauthorized(AuthData theAuthData) {
        if (theAuthData == null) {
            return new Result("Error: unauthorized");
        }

        return null;
    }

    public AuthDAO getAuthData() {
        return myAuthData;
    }
}
