package handler;

import request.LoginRequest;
import serialization.Deserializer;
import service.LoginService;
import spark.Request;
import spark.Response;
import result.LoginResult;

public class LoginHandler extends Handler {
    private final LoginService myLoginService;

    public LoginHandler(LoginService theLoginService) {
        myLoginService = theLoginService;
    }

    public Object login(Request theRequest, Response theResponse) {
        var deserializeLoginInfo = new Deserializer(theRequest.body(), new LoginRequest("", ""));

        LoginRequest loginInfo = (LoginRequest) deserializeLoginInfo.deserialize();

        LoginResult loginResult = myLoginService.login(loginInfo);

        theResponse.status(getStatusCode(loginResult));
        return getSerializedResult(loginResult);
    }
}