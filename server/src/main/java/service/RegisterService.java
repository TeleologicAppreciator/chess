package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

import java.util.UUID;

public class RegisterService {
    private UserDAO myUserData;

    public RegisterService(UserDAO userData) {
        myUserData = userData;
    }

    public RegisterResult registerUser(RegisterRequest theUserLoginData) {
        UserData registerData = new UserData(theUserLoginData.username(), theUserLoginData.password(), theUserLoginData.email());

        //UserData checkIfAlreadyUser = myUserData.getUser(registerData.myUsername());

        //if (checkIfAlreadyUser == null) {
            //myUserData.createUser(registerData);
        //}
        myUserData.createUser(registerData);
        String username = registerData.myUsername();

        var authToken = UUID.randomUUID().toString();

        AuthData authData = new AuthData(authToken, username);

        return new RegisterResult(username, authToken);
    }
}
