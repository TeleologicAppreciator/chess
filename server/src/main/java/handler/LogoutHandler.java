package handler;

import request.LogoutRequest;
import result.Result;
import serialization.Serializer;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private LogoutService myLogoutService;

    public LogoutHandler(LogoutService theLogoutService) {
        myLogoutService = theLogoutService;
    }

    public Object logout(Request theRequest, Response theResponse) {
        LogoutRequest logoutRequest = new LogoutRequest(theRequest.headers("Authorization"));

        Result logoutResult = myLogoutService.logout(logoutRequest);

        if (logoutResult.getErrorMessage() != null) {
            theResponse.status(401);
            Serializer errorLogoutSerializer = new Serializer(logoutResult);
            return errorLogoutSerializer.serialize();
        }

        theResponse.status(200);
        return "";
    }
}
