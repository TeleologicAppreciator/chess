package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import result.LoginResult;

import java.util.UUID;

public class LoginService {
    UserDAO myUserData;
    AuthDAO myAuthData;

    public LoginService(UserDAO theUserData, AuthDAO theAuthData) {
        myUserData = theUserData;
        myAuthData = theAuthData;
    }

    public LoginResult login(LoginRequest theLoginData) {
        UserData userToLogin = myUserData.getUser(theLoginData.myUsername());

        //if null its an error

        //if passwords don't match its an authentication error

        String authToken = UUID.randomUUID().toString();
        AuthData newLoginAuthentication = new AuthData(authToken, userToLogin.username());
        myAuthData.createAuth(newLoginAuthentication);

        return new LoginResult(newLoginAuthentication.username(), newLoginAuthentication.authToken());
    }
}
