package handler;

import request.LoginRequest;
import request.RegisterRequest;
import result.RegisterResult;
import serialization.Deserializer;
import serialization.Serializer;
import service.LoginService;
import spark.Request;
import spark.Response;
import result.LoginResult;

public class LoginHandler {
    private LoginService myLoginService;

    public LoginHandler(LoginService theLoginService) {
        myLoginService = theLoginService;
    }

    public Object login(Request theRequest, Response theResponse) {
        LoginRequest loginInfo;
        var deserializeLoginInfo = new Deserializer(theRequest.body(), new LoginRequest("", ""));

        loginInfo = (LoginRequest) deserializeLoginInfo.deserialize();

        LoginResult loginResult = myLoginService.login(loginInfo);

        if(loginResult.getUsername() == null) {
            theResponse.status(401);
        }

        Serializer loginSerializer = new Serializer(loginResult);
        return loginSerializer.serialize();
    }
}
