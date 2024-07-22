package handler;

import request.RegisterRequest;
import result.RegisterResult;
import serialization.Deserializer;
import serialization.Serializer;
import service.RegisterService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class RegisterHandler {
    private RegisterService myRegisterService;

    public RegisterHandler(RegisterService registerService) {
        myRegisterService = registerService;
    }

    public Object register(Request theRequest, Response theResponse) {
        RegisterRequest registerInfo;
        var deserializeRegisterInfo = new Deserializer(theRequest.body(), new RegisterRequest("", "", ""));

        registerInfo = (RegisterRequest) deserializeRegisterInfo.deserialize();

        RegisterResult registerResult = myRegisterService.registerUser(registerInfo);

        Serializer registerSerializer = new Serializer(registerResult);
        return registerSerializer.serialize();
    }
}
