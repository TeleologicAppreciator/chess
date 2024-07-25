package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import result.Result;
import result.UserResult;

import java.util.UUID;

public class LoginService {
    UserDAO myUserData;
    AuthDAO myAuthData;

    public LoginService(UserDAO theUserData, AuthDAO theAuthData) {
        myUserData = theUserData;
        myAuthData = theAuthData;
    }

    public Result login(LoginRequest theLoginData) {
        UserData userToLogin;

        try {
            userToLogin = myUserData.getUser(theLoginData.username(), theLoginData.password());
        } catch (DataAccessException e) {
            return new Result("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData newLoginAuthentication = new AuthData(authToken, userToLogin.username());
        myAuthData.createAuth(newLoginAuthentication);

        return new UserResult(newLoginAuthentication.username(), newLoginAuthentication.authToken());
    }
}
