package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.Result;
import result.UserResult;

import java.util.UUID;

public class RegisterService {
    private final UserDAO myUserData;
    private final AuthDAO myAuthData;

    public RegisterService(UserDAO userData, AuthDAO authData) {
        myUserData = userData;
        myAuthData = authData;
    }

    public Result registerUser(RegisterRequest theUserLoginData) {
        UserData registerData = new UserData(
                theUserLoginData.username(), theUserLoginData.password(), theUserLoginData.email());

        try {
            myUserData.createUser(registerData);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("User already exists")) {
                return new Result("Error: already taken");
            } else if (e.getMessage().equals("Username and password are required")) {
                return new Result("Error: bad request");
            }
        }

        String username = registerData.username();

        var authToken = UUID.randomUUID().toString();

        AuthData authData = new AuthData(authToken, username);
        try {
            myAuthData.createAuth(authData);
        } catch (Exception e) {
            return new Result("Unable to read data");
        }

        return new UserResult(username, authToken);
    }
}
