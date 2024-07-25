package handler;

import request.LoginRequest;
import result.Result;
import serialization.Deserializer;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    private final LoginService myLoginService;

    public LoginHandler(LoginService theLoginService) {
        myLoginService = theLoginService;
    }

    public Object login(Request theRequest, Response theResponse) {
        var deserializeLoginInfo = new Deserializer(theRequest.body(), new LoginRequest("", ""));

        LoginRequest loginInfo = (LoginRequest) deserializeLoginInfo.deserialize();

        Result loginResult = myLoginService.login(loginInfo);

        theResponse.status(getStatusCode(loginResult));
        return getSerializedResult(loginResult);
    }
}
