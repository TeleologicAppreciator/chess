package handler;

import com.google.gson.Gson;
import server.request.RegisterRequest;
import server.result.Result;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    private final RegisterService myRegisterService;

    public RegisterHandler(RegisterService registerService) {
        myRegisterService = registerService;
    }

    public Object register(Request theRequest, Response theResponse) {
        RegisterRequest registerRequest = new Gson().fromJson(theRequest.body(), RegisterRequest.class);

        Result registerResult = myRegisterService.registerUser(registerRequest);

        theResponse.status(getStatusCode(registerResult));
        return new Gson().toJson(registerResult);
    }
}
