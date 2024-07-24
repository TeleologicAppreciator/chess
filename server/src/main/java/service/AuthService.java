package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class AuthService {
    private final AuthDAO myAuthData;

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

    public boolean isNotAuthorized(String theAuthToken) {
        AuthData authData = userAuthorizedVerification(theAuthToken);
        return authData == null;
    }

    public AuthDAO getAuthData() {
        return myAuthData;
    }
}
