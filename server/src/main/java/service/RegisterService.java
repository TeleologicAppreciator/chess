package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
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
        if (!isUsernameValid(theUserLoginData.username()) || !isPasswordValid(theUserLoginData.password())) {
            return new Result("Error: bad request");
        }

        String hashedPassword = BCrypt.hashpw(theUserLoginData.password(), BCrypt.gensalt());

        UserData registerData = new UserData(
                theUserLoginData.username(), hashedPassword, theUserLoginData.email());

        UserData checkToSeeIfUserIsTaken = null;

        try {
            checkToSeeIfUserIsTaken = myUserData.getUser(theUserLoginData.username());
        } catch (Exception e) {
            if (e.getMessage().equals("User not found")) {
                try {
                    myUserData.createUser(registerData);
                } catch (Exception e1) {
                    return new Result("Error: " + e.getMessage());
                }
            } else {
                return new Result("Error: " + e.getMessage());
            }
        }

        if (checkToSeeIfUserIsTaken != null) {
            return new Result("Error: already taken");
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

    private boolean isUsernameValid(String username) {
        return username != null && username.matches("[a-zA-Z0-9!?]+");
    }

    private boolean isPasswordValid(String password) {
        return isUsernameValid(password);
    }
}
