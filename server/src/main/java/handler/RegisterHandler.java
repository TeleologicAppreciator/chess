package handler;

import request.RegisterRequest;
import result.Result;
import serialization.Deserializer;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    private final RegisterService myRegisterService;

    public RegisterHandler(RegisterService registerService) {
        myRegisterService = registerService;
    }

    public Object register(Request theRequest, Response theResponse) {
        var deserializeRegisterRequest = new Deserializer(theRequest.body(), new RegisterRequest("", "", ""));
        RegisterRequest registerRequest = (RegisterRequest) deserializeRegisterRequest.deserialize();

        Result registerResult = myRegisterService.registerUser(registerRequest);

        theResponse.status(getStatusCode(registerResult));
        return getSerializedResult(registerResult);
    }
}
