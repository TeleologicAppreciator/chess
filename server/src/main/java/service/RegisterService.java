package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.UUID;

public class RegisterService {
    private UserDAO myUserData;
    private AuthDAO myAuthData;

    public RegisterService(UserDAO userData, AuthDAO authData) {
        myUserData = userData;
        myAuthData = authData;
    }

    public RegisterResult registerUser(RegisterRequest theUserLoginData) {
        UserData registerData = new UserData(
                theUserLoginData.username(), theUserLoginData.password(), theUserLoginData.email());

        //UserData checkIfAlreadyUser = myUserData.getUser(registerData.username());

        //if (checkIfAlreadyUser == null) {
            //myUserData.createUser(registerData);
        //}
        myUserData.createUser(registerData);
        String username = registerData.username();

        var authToken = UUID.randomUUID().toString();

        AuthData authData = new AuthData(authToken, username);
        myAuthData.createAuth(authData);

        return new RegisterResult(username, authToken);
    }
}
