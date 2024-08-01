package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
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
            userToLogin = myUserData.getUser(theLoginData.username());
        } catch (DataAccessException e) {
            return new Result("Error: unauthorized");
        }

        if(!BCrypt.checkpw(theLoginData.password(), userToLogin.password())) {
            return new Result("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData newLoginAuthentication = new AuthData(authToken, userToLogin.username());

        try {
            myAuthData.createAuth(newLoginAuthentication);
        } catch (Exception e) {
            return new Result("Unable to read data");
        }

        return new UserResult(newLoginAuthentication.username(), newLoginAuthentication.authToken());
    }
}
