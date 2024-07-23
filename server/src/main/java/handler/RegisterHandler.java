package handler;

import request.RegisterRequest;
import result.RegisterResult;
import result.Result;
import serialization.Deserializer;
import serialization.Serializer;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    private RegisterService myRegisterService;

    public RegisterHandler(RegisterService registerService) {
        myRegisterService = registerService;
    }

    public Object register(Request theRequest, Response theResponse) {
        var deserializeRegisterInfo = new Deserializer(theRequest.body(), new RegisterRequest("", "", ""));

        RegisterRequest registerInfo = (RegisterRequest) deserializeRegisterInfo.deserialize();

        Result registerResult = myRegisterService.registerUser(registerInfo);

        String errorCode = registerResult.getErrorMessage();
        if (errorCode != null) {
            if(errorCode.equals("Error: already taken")) {
                theResponse.status(403);
            } else if (errorCode.equals("Error: bad request")) {
                theResponse.status(400);
            }
        } else {
            theResponse.status(200);
        }

        Serializer registerSerializer = new Serializer(registerResult);
        return registerSerializer.serialize();
    }
}
