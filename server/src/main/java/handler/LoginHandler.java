package handler;

import com.google.gson.Gson;
import server.request.LoginRequest;
import server.result.Result;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
    private final LoginService myLoginService;

    public LoginHandler(LoginService theLoginService) {
        myLoginService = theLoginService;
    }

    public Object login(Request theRequest, Response theResponse) {
        LoginRequest loginRequest = new Gson().fromJson(theRequest.body(), LoginRequest.class);

        Result loginResult = myLoginService.login(loginRequest);

        theResponse.status(getStatusCode(loginResult));
        return new Gson().toJson(loginResult);
    }
}
